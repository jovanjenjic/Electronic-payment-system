package com.ws.sep.paypalservice.controllers;

import com.paypal.base.rest.PayPalRESTException;
import com.ws.sep.paypalservice.dto.*;
import com.ws.sep.paypalservice.enums.FieldType;
import com.ws.sep.paypalservice.services.SellerInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping( "/api" )
public class PaypalController
{

    Logger logger = LoggerFactory.getLogger(PaypalController.class);

    @Autowired
    SellerInfoService sellerInfoService;

    @GetMapping( value = "/form" )
    public ResponseEntity<?> getForm()
    {
        logger.info("INFO - /form");
        HashMap<String, Field> returnMap = new HashMap<>();

        returnMap.put("client_id", new Field(FieldType.STRING, "client_id"));
        returnMap.put("client_secret", new Field(FieldType.STRING, "client_secret"));

        return new ResponseEntity<>(returnMap, HttpStatus.OK);
    }


    @PostMapping(value = "/addPayment")
    public ResponseEntity<?> addPayment(@RequestBody SellerInfoDTO sellerInfoDTO, @RequestHeader("Authorization") String token) {
        logger.info("INFO - /addPayment");

        sellerInfoService.addPaymentCredentials(sellerInfoDTO, token);

        HashMap<String, String> retMessage = new HashMap<>();
        retMessage.put("status", "success");
        retMessage.put("message", "payment method added successfully!");

        return new ResponseEntity<>(retMessage, HttpStatus.CREATED);
    }

    @PostMapping(value = "/pay")
    public ResponseEntity<?> pay(@RequestBody OrderDTO orderDTO, @RequestHeader("Authorization") String token) throws PayPalRESTException {
        logger.info("INFO - /pay");

        String url = sellerInfoService.createPayment(orderDTO, token);

        HashMap<String, String> retObj = new HashMap<>();

        HttpStatus status;
        if (url.equals("")) {
            retObj.put("message", "Failed to create pay");
            status = HttpStatus.EXPECTATION_FAILED;
        } else {
            retObj.put("paymentUrl", url);
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(retObj, status);
    }

    @PostMapping(value = "/pay/{id}/success")
    public ResponseEntity<?> successPay(@RequestBody ExecutePaymentDTO executePaymentDTO, @PathVariable("id") Long orderId, @RequestHeader("Authorization") String token) {
        logger.info("INFO - /pay/{id}/success");
        return sellerInfoService.executePayment(executePaymentDTO, orderId, token);
    }

    @PostMapping(value = "/pay/{id}/cancel")
    public ResponseEntity<?> cancelPay(@PathVariable("id") Long orderId) {
        logger.info("INFO - /pay/{id}/cancel");
        return sellerInfoService.cancelOrderPayment(orderId);
    }

    @PostMapping(value = "/billingPlan")
    public ResponseEntity<?> createBillingPlan(@RequestBody BillingPlanDTO billingPlanDTO,  @RequestHeader("Authorization") String token) throws PayPalRESTException {
        logger.info("INFO - /billingPlan");
        return sellerInfoService.createBillingPlan(billingPlanDTO, token);
    }

    @PostMapping(value = "/subscription")
    public ResponseEntity<?> createSubscription(@RequestBody SubscriptionDTO subscriptionDTO, @RequestHeader("Authorization") String token) {
        logger.info("INFO - /subscription");
        return sellerInfoService.createSubscription(subscriptionDTO, token);
    }

    @PostMapping(value = "/subscription/{id}/success")
    public ResponseEntity<?> executeSubscription(@RequestBody ExecuteSubscriptionDTO executeSubscriptionDTO, @PathVariable("id") Long subscriptionId, @RequestHeader("Authorization") String token) throws PayPalRESTException {
        logger.info("INFO - /pay");
        return sellerInfoService.executeSubscription(executeSubscriptionDTO, subscriptionId, token);
    }

    @PostMapping(value = "/subscription/{id}/cancel")
    public ResponseEntity<?> cancelSubscription(@PathVariable("id") Long subscriptionId, @RequestHeader("Authorization") String token) {
        logger.info("INFO - /subscription/{id}/success");
        return sellerInfoService.cancelSubscription(subscriptionId, token);
    }

}
