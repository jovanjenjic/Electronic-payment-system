package com.ws.sep.paypalservice.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.ws.sep.paypalservice.dto.ExecutePaymentDTO;
import com.ws.sep.paypalservice.dto.OrderDTO;
import com.ws.sep.paypalservice.dto.SellerInfoDTO;
import com.ws.sep.paypalservice.enums.OrderState;
import com.ws.sep.paypalservice.exceptions.AlreadyExistsException;
import com.ws.sep.paypalservice.exceptions.InvalidValueException;
import com.ws.sep.paypalservice.exceptions.SimpleException;
import com.ws.sep.paypalservice.model.SellerOrders;
import com.ws.sep.paypalservice.model.SellerInfo;
import com.ws.sep.paypalservice.repository.SellerOrderRepository;
import com.ws.sep.paypalservice.repository.SellerInfoRepository;
import com.ws.sep.paypalservice.utils.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import utils.JwtUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SellerInfoService {

    @Autowired
    SellerInfoRepository sellerInfoRepository;

    @Autowired
    SellerOrderRepository sellerOrderRepository;

    @Autowired
    JwtUtil jwtUtil;

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
            sellerOrders.setOrderState(OrderState.PENDING);
            sellerOrderRepository.save(sellerOrders);
            return linksOptional.get().getHref();
        }

        sellerOrders.setOrderState(OrderState.FAILED);
        sellerOrderRepository.save(sellerOrders);
        return "";
    }

    public void executePaymenr(ExecutePaymentDTO executePaymentDTO, Long orderId, String token) {
        Long sellerId = jwtUtil.extractSellerId(token.substring(7));

        Optional<SellerOrders> optionalOrder = sellerOrderRepository.findById(orderId);

        if(optionalOrder.isEmpty())
            throw new SimpleException(404, "No specified order");

        SellerOrders order = optionalOrder.get();

        if(Arrays.asList(OrderState.SUCCESS, OrderState.FAILED, OrderState.CANCELED).stream().anyMatch(t -> t.equals(order.getOrderState()))) {
            throw new SimpleException(400, "Order is not valid for paymeent!");
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
        sellerOrderRepository.save(order);
    }

    public void cancelOrderPayment(Long orderId) {
        Optional<SellerOrders> orderOptional = sellerOrderRepository.findById(orderId);

        if(orderOptional.isEmpty())
            throw new SimpleException(404, "No specified order");

        SellerOrders order = orderOptional.get();
        if(order.getOrderState().equals(OrderState.CANCELED))
            throw new SimpleException(400, "Order already cancelled");

        order.setOrderState(OrderState.CANCELED);
        sellerOrderRepository.save(order);
    }

}
