package com.ws.sep.bitcoinservice.services;

import com.netflix.discovery.converters.Auto;
import com.ws.sep.bitcoinservice.dto.*;
import com.ws.sep.bitcoinservice.enums.TransactionStatus;
import com.ws.sep.bitcoinservice.exceptions.InvalidValueException;
import com.ws.sep.bitcoinservice.exceptions.SimpleException;
import com.ws.sep.bitcoinservice.model.CryptocurrencyPayment;
import com.ws.sep.bitcoinservice.model.PaymentInformation;
import com.ws.sep.bitcoinservice.repository.CryptocurrencyRepository;
import com.ws.sep.bitcoinservice.repository.PaymentInformarmationRepository;
import com.ws.sep.bitcoinservice.utility.FieldType;
import com.ws.sep.bitcoinservice.utility.JwtUtil;
import com.ws.sep.bitcoinservice.utility.GlobalDataString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CryptocurrencyService {

    @Autowired
    CryptocurrencyRepository cryptocurrencyRepository;

    @Autowired
    PaymentInformarmationRepository paymentInformarmationRepository;

    @Autowired
    JwtUtil jwtUtil;

    public  HashMap getFormForPayment() {
        HashMap<String, Field> formData = new HashMap<>();
        formData.put("apiKey", new Field(FieldType.STRING, "apiKey"));
        return formData;
   }

    @Transactional
    public HashMap registerPayment(SellerInfoDTO sellerInfoDTO, String authToken) throws FileAlreadyExistsException {
        Long sellerId = jwtUtil.extractSellerId(authToken.substring(7));

        if(cryptocurrencyRepository.existsBySellerId(sellerId))
            throw new FileAlreadyExistsException("Payment already exists for the user");

        if(sellerInfoDTO.getApiKey() == null || sellerInfoDTO.getApiKey().isEmpty())
            throw new InvalidValueException("Seller api key", "SellerApiKey cannot be null");


        /* used for creating type inside sellers */
        String url = GlobalDataString.LOCALHOST + "/seller/api/payment";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);

        Map<String, String> reqObj = new HashMap<>();
        reqObj.put("type", "BITCOIN");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(reqObj, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(url, entity, Void.class);

        if(response.getStatusCode() != HttpStatus.OK) throw new SimpleException(400, "Failed to add type");

        CryptocurrencyPayment data = new CryptocurrencyPayment();
        data.setSellerId(sellerId);
        data.setApiKey(sellerInfoDTO.getApiKey());

        cryptocurrencyRepository.save(data);

        HashMap<String, String> retMessage = new HashMap<>();
        retMessage.put("status", "success");
        retMessage.put("message", "payment method added successfully!");


        return retMessage;
    }

    @Transactional
    public CreateOrderResponseDTO createOrder(TransactionDetailsDTO transactionDetailsDTO, String token) throws FileAlreadyExistsException {
        Long sellerId = jwtUtil.extractSellerId(token.substring(7));


        CryptocurrencyPayment cryptocurrencyPayment = cryptocurrencyRepository.findOneBySellerId(sellerId);

        if(!cryptocurrencyRepository.existsBySellerId(sellerId))
            throw new FileAlreadyExistsException("Seller does not have permission");

        if(transactionDetailsDTO.getPriceAmount() == null || transactionDetailsDTO.getPriceAmount().isEmpty())
            throw new InvalidValueException("priceAmount", "Price amount data is incorrect");

        if(transactionDetailsDTO.getPriceCurrency() == null || transactionDetailsDTO.getPriceCurrency().isEmpty())
            throw new InvalidValueException("priceCurrency", "Price currency data is incorrect");

        if(transactionDetailsDTO.getReceiveCurrency() == null || transactionDetailsDTO.getReceiveCurrency().isEmpty())
            throw new InvalidValueException("receiveCurrency", "Receive currency data is incorrect");

        LocalDateTime ld = LocalDateTime.now();

        PaymentInformation paymentInformation = new PaymentInformation();
        paymentInformation.setStatus(TransactionStatus.NEW.name());
        paymentInformation.setPriceAmount(transactionDetailsDTO.getPriceAmount());
        paymentInformation.setPriceCurrency(transactionDetailsDTO.getPriceCurrency());
        paymentInformation.setReceiveCurrency(transactionDetailsDTO.getReceiveCurrency());
        paymentInformation.setOrderId(transactionDetailsDTO.getOrderId());
        paymentInformation.setCryptocurrencyPayment(cryptocurrencyPayment);
        paymentInformation.setCreatedAt(ld);

        paymentInformarmationRepository.save(paymentInformation);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + cryptocurrencyPayment.getApiKey());

        Map<String, String> params = new HashMap<>();
        params.put("order_id", transactionDetailsDTO.getOrderId());
        params.put("price_amount", transactionDetailsDTO.getPriceAmount());
        params.put("price_currency", transactionDetailsDTO.getPriceCurrency());
        params.put("receive_currency", transactionDetailsDTO.getReceiveCurrency());

        params.put("title", transactionDetailsDTO.getTitle());
        params.put("description", transactionDetailsDTO.getDescription());
        params.put("success_url", GlobalDataString.DASHBOARD + "/payments/" + paymentInformation.getId() + "/bitcoin/success");
        params.put("cancel_url", GlobalDataString.DASHBOARD + "/payments/" + paymentInformation.getId() + "/bitcoin/cancel");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<CreateOrderResponseDTO> response = restTemplate.postForEntity(GlobalDataString.SENDBOX_ORDERS, entity, CreateOrderResponseDTO.class);

        paymentInformation.setPaymentId(response.getBody().getId());
        paymentInformarmationRepository.save(paymentInformation);

        return response.getBody();
    }

    @Transactional
    public CheckoutOrderResponseDTO checkoutOrder(CheckoutOrderDTO checkoutOrderDTO, String token) throws FileAlreadyExistsException {
        Long sellerId = jwtUtil.extractSellerId(token.substring(7));

        CryptocurrencyPayment cryptocurrencyPayment = cryptocurrencyRepository.findOneBySellerId(sellerId);

        if(!cryptocurrencyRepository.existsBySellerId(sellerId))
            throw new FileAlreadyExistsException("Seller does not have permission");

        if(checkoutOrderDTO.getPayCurrency() == null || checkoutOrderDTO.getPayCurrency().isEmpty())
            throw new InvalidValueException("priceAmount", "Price amount data is incorrect");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + cryptocurrencyPayment.getApiKey());

        Map<String, String> params = new HashMap<>();
        params.put("id", checkoutOrderDTO.getOrderId());
        params.put("pay_currency", checkoutOrderDTO.getPayCurrency());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<CheckoutOrderResponseDTO> response = restTemplate.postForEntity(GlobalDataString.SENDBOX_ORDERS + "/" + checkoutOrderDTO.getOrderId() + "/checkout", entity, CheckoutOrderResponseDTO.class);

        return response.getBody();
    }

    @Transactional
    public GetOrderResponseDTO getOrder(OrderIdDTO orderIdDTO, String token) throws FileAlreadyExistsException {
        Long sellerId = jwtUtil.extractSellerId(token.substring(7));
        CryptocurrencyPayment cryptocurrencyPayment = cryptocurrencyRepository.findOneBySellerId(sellerId);

        if (orderIdDTO.getOrderId() == null || orderIdDTO.getOrderId().isEmpty())
            throw new InvalidValueException("sellerId", "Seller id data is incorrect");

        if(!cryptocurrencyRepository.existsBySellerId(sellerId))
            throw new FileAlreadyExistsException("Seller does not have permission");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + cryptocurrencyPayment.getApiKey());

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<GetOrderResponseDTO> response = restTemplate.exchange(GlobalDataString.SENDBOX_ORDERS + "/" + orderIdDTO.getOrderId(), HttpMethod.GET, entity, GetOrderResponseDTO.class);

        return response.getBody();
    }

    @Transactional
    public ListOfOrderResponseDTO listAllPaymentOfSeller(String token) throws FileAlreadyExistsException {
        Long sellerId = jwtUtil.extractSellerId(token.substring(7));
        CryptocurrencyPayment cryptocurrencyPayment = cryptocurrencyRepository.findOneBySellerId(sellerId);

        if(!cryptocurrencyRepository.existsBySellerId(sellerId))
            throw new FileAlreadyExistsException("Seller does not have permission");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + cryptocurrencyPayment.getApiKey());

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<ListOfOrderResponseDTO> response = restTemplate.exchange(GlobalDataString.SENDBOX_ORDERS, HttpMethod.GET, entity, ListOfOrderResponseDTO.class);

        return response.getBody();
    }

    @Transactional
    public HashMap setStateOnSuccessOrCancel(SuccessCancelDTO successCancelDTO, String authToken) throws FileAlreadyExistsException {
        Long sellerId = jwtUtil.extractSellerId(authToken.substring(7));

        if(successCancelDTO.getTransactionId() == null)
            throw new InvalidValueException("transactionId", "TransactionId cannot be null or empty");

        PaymentInformation paymentInformation = paymentInformarmationRepository.findOneById(successCancelDTO.getTransactionId());
        if(paymentInformation == null)
            throw new FileAlreadyExistsException("Payment don't exists id DB");

        if(successCancelDTO.getIsSuccess()) {
            paymentInformation.setStatus(TransactionStatus.PAID.name());
        } else {
            paymentInformation.setStatus(TransactionStatus.CANCELED.name());
        }
        paymentInformarmationRepository.save(paymentInformation);

        HashMap<String, String> retMessage = new HashMap<>();
        retMessage.put("status", "success");
        retMessage.put("message", "status changed successfully!");

        return retMessage;
    }
}
