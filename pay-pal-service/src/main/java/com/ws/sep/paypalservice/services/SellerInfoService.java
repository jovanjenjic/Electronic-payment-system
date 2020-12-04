package com.ws.sep.paypalservice.services;

import com.ws.sep.paypalservice.dto.SellerInfoDTO;
import com.ws.sep.paypalservice.exceptions.AlreadyExistsException;
import com.ws.sep.paypalservice.exceptions.InvalidValueException;
import com.ws.sep.paypalservice.exceptions.SimpleException;
import com.ws.sep.paypalservice.model.SellerInfo;
import com.ws.sep.paypalservice.repository.SellerInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import utils.JwtUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SellerInfoService {

    @Autowired
    SellerInfoRepository sellerInfoRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Transactional
    public void addPayment(SellerInfoDTO sellerInfoDTO, String authToken) {
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
}
