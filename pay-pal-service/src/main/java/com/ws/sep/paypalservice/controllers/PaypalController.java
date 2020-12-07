package com.ws.sep.paypalservice.controllers;

import com.paypal.base.rest.PayPalRESTException;
import com.ws.sep.paypalservice.dto.*;
import com.ws.sep.paypalservice.enums.FieldType;
import com.ws.sep.paypalservice.services.SellerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping( "/api" )
public class PaypalController
{

    @Autowired
    SellerInfoService sellerInfoService;

    @GetMapping( value = "/form" )
    public ResponseEntity<?> getForm()
    {
        HashMap<String, Field> returnMap = new HashMap<>();

        returnMap.put("client_id", new Field(FieldType.STRING, "client_id"));
        returnMap.put("client_secret", new Field(FieldType.STRING, "client_secret"));

        return new ResponseEntity<>(returnMap, HttpStatus.OK);
    }


    @PostMapping(value = "/addPayment")
    public ResponseEntity<?> addPayment(@RequestBody SellerInfoDTO sellerInfoDTO, @RequestHeader("Authorization") String token) {
        System.out.println(sellerInfoDTO.toString());

        sellerInfoService.addPaymentCredentials(sellerInfoDTO, token);

        HashMap<String, String> retMessage = new HashMap<>();
        retMessage.put("status", "success");
        retMessage.put("message", "payment method added successfully!");

        return new ResponseEntity<>(retMessage, HttpStatus.CREATED);
    }

    @PostMapping(value = "/pay")
    public ResponseEntity<?> pay(@RequestBody OrderDTO orderDTO, @RequestHeader("Authorization") String token) throws PayPalRESTException {
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
        sellerInfoService.executePaymenr(executePaymentDTO, orderId, token);

        HashMap<String, String> retObj = new HashMap<>();
        retObj.put("message", "payment executed successfully");

        return new ResponseEntity<>(retObj, HttpStatus.OK);
    }

    @PostMapping(value = "/pay/{id}/cancel")
    public ResponseEntity<?> cancelPay(@PathVariable("id") Long orderId) {
        sellerInfoService.cancelOrderPayment(orderId);

        HashMap<String, String> retObj = new HashMap<>();
        retObj.put("message", "payment cancelled successfully");

        return new ResponseEntity<>(retObj, HttpStatus.OK);
    }

    @PostMapping(value = "/billingPlan")
    public ResponseEntity<?> createBillingPlan(@RequestBody BillingPlanDTO billingPlanDTO,  @RequestHeader("Authorization") String token) throws PayPalRESTException {
        return sellerInfoService.createBillingPlan(billingPlanDTO, token);
    }

    @PostMapping(value = "/subscription")
    public ResponseEntity<?> createSubscription(@RequestBody SubscriptionDTO subscriptionDTO, @RequestHeader("Authorization") String token) {
        return sellerInfoService.createSubscription(subscriptionDTO, token);
    }

    @PostMapping(value = "/subscription/{id}/success")
    public ResponseEntity<?> executeSubscription(@RequestBody ExecuteSubscriptionDTO executeSubscriptionDTO, @PathVariable("id") Long subscriptionId, @RequestHeader("Authorization") String token) throws PayPalRESTException {
        return sellerInfoService.executeSubscription(executeSubscriptionDTO, subscriptionId, token);
    }

    @PostMapping(value = "/subscription/{id}/cancel")
    public ResponseEntity<?> cancelSubscription(@PathVariable("id") Long subscriptionId, @RequestHeader("Authorization") String token) {
        return sellerInfoService.cancelSubscription(subscriptionId, token);
    }

}
