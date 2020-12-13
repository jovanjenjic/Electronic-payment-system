package com.ws.sep.paypalservice.services;

import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.ws.sep.paypalservice.dto.*;
import com.ws.sep.paypalservice.enums.OrderState;
import com.ws.sep.paypalservice.enums.PlanState;
import com.ws.sep.paypalservice.enums.SubscriptionState;
import com.ws.sep.paypalservice.exceptions.AlreadyExistsException;
import com.ws.sep.paypalservice.exceptions.InvalidValueException;
import com.ws.sep.paypalservice.exceptions.SimpleException;
import com.ws.sep.paypalservice.mappers.BillingPlanMapper;
import com.ws.sep.paypalservice.mappers.SellerOrderMapper;
import com.ws.sep.paypalservice.mappers.SubscriptionMapper;
import com.ws.sep.paypalservice.model.BillingPlan;
import com.ws.sep.paypalservice.model.SellerOrders;
import com.ws.sep.paypalservice.model.SellerInfo;
import com.ws.sep.paypalservice.model.Subscription;
import com.ws.sep.paypalservice.repository.BillingPlanRepository;
import com.ws.sep.paypalservice.repository.SellerOrderRepository;
import com.ws.sep.paypalservice.repository.SellerInfoRepository;
import com.ws.sep.paypalservice.repository.SubscriptionRepository;
import com.ws.sep.paypalservice.utils.Urls;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import utils.JwtUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class SellerInfoService {

    @Autowired
    SellerInfoRepository sellerInfoRepository;

    @Autowired
    SellerOrderRepository sellerOrderRepository;

    @Autowired
    BillingPlanRepository billingPlanRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    JwtUtil jwtUtil;

    String subscriptionSuccessUrl = Urls.DASHBOARD_URL + "/payments/paypal/subscriptions/{id}/success";
    String subscriptionCancelUrl = Urls.DASHBOARD_URL + "/payments/paypal/subscriptions/{id}/cancel";

    @Transactional
    public void addPaymentCredentials(SellerInfoDTO sellerInfoDTO, String authToken) {
        Long sellerId = jwtUtil.extractSellerId(authToken.substring(7));

        // check if payment already exists
        if(sellerInfoRepository.existsBySellerId(sellerId))
            throw new AlreadyExistsException("Payment already exists for the user");

        if(sellerInfoDTO.getClient_id() == null || sellerInfoDTO.getClient_id().isEmpty()) {
            throw new InvalidValueException("client_id", "client_id cannot be null or empty");
        }

        if(sellerInfoDTO.getClient_secret() == null || sellerInfoDTO.getClient_secret().isEmpty()) {
            throw new InvalidValueException("client_secret", "client_secret cannot be null");
        }

        // used for creating type inside sellers
        String url = "https://localhost:8765/seller/api/payment";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);

        Map<String, String> reqObj = new HashMap<>();
        reqObj.put("type", "PAYPAL");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(reqObj, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(url, entity, Void.class);

        if(response.getStatusCode() != HttpStatus.OK) throw new SimpleException(400, "Failed to add type");

        // if everything is okay add to the

        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setSellerId(sellerId);
        sellerInfo.setClient_id(sellerInfoDTO.getClient_id());
        sellerInfo.setClient_secret(sellerInfoDTO.getClient_secret());

        sellerInfoRepository.save(sellerInfo);
    }


    public APIContext getApiContext(Long sellerId) throws PayPalRESTException {
        // check if paypal exists for the specified user
        if(!sellerInfoRepository.existsBySellerId(sellerId))
            throw new SimpleException(404, "Configuration not exists for the seller");

        SellerInfo sellerInfo = sellerInfoRepository.findOneBySellerId(sellerId);

        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", "sandbox");

        OAuthTokenCredential oAuthTokenCredential = new OAuthTokenCredential(sellerInfo.getClient_id(), sellerInfo.getClient_secret(), configMap);

        APIContext context = new APIContext(oAuthTokenCredential.getAccessToken());
        context.setConfigurationMap(configMap);

        return context;
    }

    @Transactional
    public String createPayment(OrderDTO orderDTO, String token) throws PayPalRESTException {
        Long sellerId = jwtUtil.extractSellerId(token.substring(7));

        APIContext context = getApiContext(sellerId);

        // calculate total amount for the sellerOrders
        Double total = new BigDecimal(orderDTO.getPrice() * orderDTO.getItems_count()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        // set amount object
        Amount amount = new Amount();
        amount.setCurrency(orderDTO.getCurrency());
        amount.setTotal(String.format(Locale.US,"%.2f", total));
        // preapare transaction object
        Transaction transaction = new Transaction();
        transaction.setDescription(orderDTO.getDescription());
        transaction.setAmount(amount);

        List<Transaction> transactionList = new ArrayList<>();

        transactionList.add(transaction);
        // create payer with payment method
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactionList);


        // create new SellerOrders
        SellerOrders sellerOrders = new SellerOrders();
        sellerOrders.setPrice(orderDTO.getPrice());
        sellerOrders.setItemsCount(orderDTO.getItems_count());
        sellerOrders.setItemId(orderDTO.getItem_id());
        sellerOrders.setCurrency(orderDTO.getCurrency());
        sellerOrders.setDescription(orderDTO.getDescription());
        sellerOrders.setOrderState(OrderState.CREATED);
        sellerOrders.setSellerId(sellerId);
        sellerOrders.setCreatedAt(LocalDateTime.now());

        // should have id after the save
        sellerOrders = sellerOrderRepository.save(sellerOrders);

        // prepare redirect urls
        RedirectUrls redirectUrls = new RedirectUrls();
        String successUrl = Urls.DASHBOARD_URL + "/payments/" + sellerOrders.getId() + "/paypal/success";
        String cancelUrl = Urls.DASHBOARD_URL + "/payments/" + sellerOrders.getId() + "/paypal/cancel";
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(context);

        // find approval url using lambda expressions
        Optional<Links> linksOptional = createdPayment.getLinks().stream().filter(link -> link.getRel().equals("approval_url")).findFirst();

        // if we have approval url
        if(linksOptional.isPresent()) {
            String paymentUrl = linksOptional.get().getHref();
            sellerOrders.setOrderState(OrderState.PENDING);
            sellerOrders.setPaymentUrl(paymentUrl);
            sellerOrderRepository.save(sellerOrders);
            return paymentUrl;
        }

        sellerOrders.setOrderState(OrderState.FAILED);
        sellerOrderRepository.save(sellerOrders);
        return "";
    }

    public ResponseEntity<?> executePayment(ExecutePaymentDTO executePaymentDTO, Long orderId, String token) {
        Long sellerId = jwtUtil.extractSellerId(token.substring(7));

        Optional<SellerOrders> optionalOrder = sellerOrderRepository.findById(orderId);

        if(optionalOrder.isEmpty())
            throw new SimpleException(404, "No specified order");

        SellerOrders order = optionalOrder.get();

        if(Arrays.asList(OrderState.SUCCESS, OrderState.FAILED, OrderState.CANCELED).stream().anyMatch(t -> t.equals(order.getOrderState()))) {
            throw new SimpleException(400, "Order is not valid for payment!");
        }

        // payment execution
        Payment payment = new Payment();
        payment.setId(executePaymentDTO.getPaymentId());
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(executePaymentDTO.getPayerId());

        try {
            payment.execute(getApiContext(sellerId), paymentExecution);
        } catch (PayPalRESTException e) {
            order.setOrderState(OrderState.FAILED);
            sellerOrderRepository.save(order);
            throw new SimpleException(417, "Failed to execute payment");
        }

        order.setOrderState(OrderState.SUCCESS);
        SellerOrders savedOrder = sellerOrderRepository.save(order);

        OrderResponse orderResponse = SellerOrderMapper.INSTANCE.mapToResponse(savedOrder);

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> cancelOrderPayment(Long orderId) {
        Optional<SellerOrders> orderOptional = sellerOrderRepository.findById(orderId);

        if(orderOptional.isEmpty())
            throw new SimpleException(404, "No specified order");

        SellerOrders order = orderOptional.get();
        if(order.getOrderState().equals(OrderState.CANCELED))
            throw new SimpleException(400, "Order already cancelled");

        order.setOrderState(OrderState.CANCELED);
        order = sellerOrderRepository.save(order);

        OrderResponse orderResponse = SellerOrderMapper.INSTANCE.mapToResponse(order);

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    // --> Billing plans and subscriptions
    public ResponseEntity<?> createBillingPlan(BillingPlanDTO billingPlanDTO, String token) throws PayPalRESTException {
        Long sellerId = jwtUtil.extractSellerId(token.substring(7));

        // plan object to be built
        Plan plan = new Plan();
        plan.setName(billingPlanDTO.getName());
        plan.setDescription(billingPlanDTO.getDescription());
        plan.setType("fixed");

        // Payment_definitions
        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setName("Regular Payments");
        paymentDefinition.setType("REGULAR");
        paymentDefinition.setFrequency("MONTH");
        paymentDefinition.setFrequencyInterval("1");
        paymentDefinition.setCycles("12");

        // Currency
        Currency currency = new Currency();
        currency.setCurrency(billingPlanDTO.getCurrency());
        currency.setValue(String.format(Locale.US,"%.2f", billingPlanDTO.getValue()));
        paymentDefinition.setAmount(currency);

        // Charge_models
        ChargeModels chargeModels = new com.paypal.api.payments.ChargeModels();
        chargeModels.setType("SHIPPING");
        chargeModels.setAmount(currency);
        List<ChargeModels> chargeModelsList = new ArrayList<>();
        chargeModelsList.add(chargeModels);
        paymentDefinition.setChargeModels(chargeModelsList);

        // Payment_definition
        List<PaymentDefinition> paymentDefinitionList = new ArrayList<>();
        paymentDefinitionList.add(paymentDefinition);
        plan.setPaymentDefinitions(paymentDefinitionList);

        // Success and cancel URLs
        String successUrl = Urls.DASHBOARD_URL + "/payments/paypal/subscriptions/success";
        String cancelUrl = Urls.DASHBOARD_URL + "/payments/paypal/subscriptions/cancel";

        MerchantPreferences merchantPreferences = new MerchantPreferences();
        merchantPreferences.setSetupFee(currency);
        merchantPreferences.setCancelUrl(cancelUrl);
        merchantPreferences.setReturnUrl(successUrl);
        merchantPreferences.setMaxFailAttempts("0");
        merchantPreferences.setAutoBillAmount("YES");
        merchantPreferences.setInitialFailAmountAction("CONTINUE");
        plan.setMerchantPreferences(merchantPreferences);

        Plan createdPlan = plan.create(getApiContext(sellerId));

        // Set up plan activate PATCH request
        List<Patch> patchRequestList = new ArrayList<>();
        Map<String, String> value = new HashMap<>();
        value.put("state", "ACTIVE");

        // Create update object to activate plan
        Patch patch = new Patch();
        patch.setPath("/");
        patch.setValue(value);
        patch.setOp("replace");
        patchRequestList.add(patch);

        createdPlan.update(getApiContext(sellerId), patchRequestList);

        BillingPlan billingPlan = BillingPlanMapper.INSTANCE.planFromDto(billingPlanDTO);
        billingPlan.setSellerId(sellerId);
        billingPlan.setPlanId(createdPlan.getId());
        billingPlan.setState(PlanState.ACTIVE);

        billingPlan = billingPlanRepository.save(billingPlan);

        BillingPlanResponse response = BillingPlanMapper.INSTANCE.planToResponse(billingPlan);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Optional<BillingPlan> getBillingPlan(SubscriptionDTO subscriptionDTO) {
        if(Optional.ofNullable(subscriptionDTO.getPlanId()).isPresent()) {
            Optional<BillingPlan> billingPlanOptional = billingPlanRepository.findById(subscriptionDTO.getPlanId());
            return billingPlanOptional;
        }

        if(Optional.ofNullable(subscriptionDTO.getItemId()).isPresent()) {
            List<BillingPlan> billingPlans = billingPlanRepository.getBillingPlanForItem(subscriptionDTO.getItemId());
            return billingPlans.stream().findFirst();
        }

        throw new SimpleException(404, "Plan is not found for specified item");
    }

    public ResponseEntity<?> createSubscription(SubscriptionDTO subscriptionDTO, String token)  {
        Long sellerId = jwtUtil.extractSellerId(token.substring(7));

        Optional<BillingPlan> billingPlanOptional = getBillingPlan(subscriptionDTO);

        if(billingPlanOptional.isEmpty())
            throw new SimpleException(404, "Plan is not found for specified item");

        BillingPlan billingPlan = billingPlanOptional.get();

        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime zdt = ldt.atZone(ZoneOffset.UTC); //you might use a different zone
        String startDate = zdt.toString();

        // start creating agreement
        Agreement agreement = new Agreement();
        agreement.setName(subscriptionDTO.getName());
        agreement.setDescription(subscriptionDTO.getDescription());
        agreement.setStartDate(startDate);

        // Set plan ID
        Plan plan = new Plan();
        plan.setId(billingPlan.getPlanId());
        agreement.setPlan(plan);

        // Add payer details
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        agreement.setPayer(payer);


        Subscription subscription = new Subscription();
        try {
            subscription.setState(SubscriptionState.PENDING);
            subscription = subscriptionRepository.save(subscription);

            // Currency
            Currency currency = new Currency();
            currency.setCurrency(billingPlan.getCurrency());
            currency.setValue(billingPlan.getValue());

            // merchant preferences for override
            MerchantPreferences merchantPreferences = new MerchantPreferences();
            merchantPreferences.setSetupFee(currency);
            merchantPreferences.setCancelUrl(subscriptionCancelUrl.replace("{id}", String.valueOf(subscription.getId())));
            merchantPreferences.setReturnUrl(subscriptionSuccessUrl.replace("{id}", String.valueOf(subscription.getId())));
            merchantPreferences.setMaxFailAttempts("0");
            merchantPreferences.setAutoBillAmount("YES");
            merchantPreferences.setInitialFailAmountAction("CONTINUE");

            agreement.setOverrideMerchantPreferences(merchantPreferences);

            agreement = agreement.create(getApiContext(sellerId));

            Optional<Links> linksOptional = agreement.getLinks().stream().filter(link -> link.getRel().equals("approval_url")).findFirst();

            // if we have approval link
            if(linksOptional.isPresent()) {
                subscription.setCreatedAt(LocalDateTime.now());
                subscription.setDescription(subscriptionDTO.getDescription());
                subscription.setName(subscriptionDTO.getName());
                subscription.setBillingPlan(billingPlan);

                subscriptionRepository.save(subscription);

                HashMap<String, String> retObj = new HashMap<>();
                retObj.put("paymentUrl", linksOptional.get().getHref());

                return new ResponseEntity<>(retObj, HttpStatus.CREATED);
            }

        } catch (PayPalRESTException e) {
            subscription.setState(SubscriptionState.CANCELLED);
            subscriptionRepository.save(subscription);
            throw new SimpleException(500, e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HashMap<String, String> retObj = new HashMap<>();
        retObj.put("message", "Failed to create subscription");

        return new ResponseEntity<>(retObj, HttpStatus.EXPECTATION_FAILED);
    }

    public ResponseEntity<?> executeSubscription(ExecuteSubscriptionDTO executeSubscriptionDTO, Long subscriptionId, String token) {
        Long sellerId = jwtUtil.extractSellerId(token.substring(7));

        Agreement agreement = new Agreement();
        agreement.setToken(executeSubscriptionDTO.getSubscriptionToken());

        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(subscriptionId);

        if(subscriptionOptional.isEmpty())
            throw new SimpleException(404, "Not found specified subscription");

        Subscription subscription = subscriptionOptional.get();

        try {
            Agreement activatedAgreement = agreement.execute(getApiContext(sellerId), agreement.getToken());

            subscription.setState(SubscriptionState.ACTIVE);
            subscription.setAgreementId(activatedAgreement.getId());

            subscription = subscriptionRepository.save(subscription);

            SubscriptionResponse subscriptionResponse = SubscriptionMapper.INSTANCE.mapToResponse(subscription);

            return new ResponseEntity<>(subscriptionResponse, HttpStatus.OK);

        } catch (PayPalRESTException e) {
            throw new SimpleException(500, e.getMessage());
        }
    }

    public ResponseEntity<?> cancelSubscription(Long subscriptionId, String token) {
        Long sellerId = jwtUtil.extractSellerId(token.substring(7));

        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(subscriptionId);

        if(subscriptionOptional.isEmpty())
            throw new SimpleException(404, "Not found specified subscription");

        Subscription subscription = subscriptionOptional.get();

        // case when subscription is ACTIVE and has agreementId
        if(subscription.getState().equals(SubscriptionState.ACTIVE) && !StringUtils.isEmpty(subscription.getAgreementId())) {
            Agreement agreement = new Agreement();
            agreement.setId(subscription.getAgreementId());

            Currency currency = new Currency();
            currency.setValue(String.format(Locale.US,"%.2f", Double.valueOf("0")));
            currency.setCurrency(subscription.getBillingPlan().getCurrency());

            AgreementStateDescriptor agreementStateDescriptor = new AgreementStateDescriptor();
            agreementStateDescriptor.setNote("Cancellation note");
            agreementStateDescriptor.setAmount(currency);

            try {
                agreement.cancel(getApiContext(sellerId), agreementStateDescriptor);
                subscription.setState(SubscriptionState.CANCELLED);
                subscription = subscriptionRepository.save(subscription);

                SubscriptionResponse subscriptionResponse = SubscriptionMapper.INSTANCE.mapToResponse(subscription);

                return new ResponseEntity<>(subscriptionResponse, HttpStatus.OK);
            } catch (PayPalRESTException e) {
                throw new SimpleException(417, e.getMessage());
            }
        }

        if(subscription.getState().equals(SubscriptionState.PENDING)) {
            subscription.setState(SubscriptionState.CANCELLED);
            subscription = subscriptionRepository.save(subscription);

            SubscriptionResponse subscriptionResponse = SubscriptionMapper.INSTANCE.mapToResponse(subscription);

            return new ResponseEntity<>(subscriptionResponse, HttpStatus.OK);
        }

        HashMap<String, String> retObj = new HashMap<>();
        retObj.put("message", "Failed to cancel subscription");

        return new ResponseEntity<>(retObj, HttpStatus.EXPECTATION_FAILED);
    }
}
