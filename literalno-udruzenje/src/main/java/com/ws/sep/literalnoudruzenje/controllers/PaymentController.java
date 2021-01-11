package com.ws.sep.literalnoudruzenje.controllers;

import com.ws.sep.literalnoudruzenje.dto.*;
import com.ws.sep.literalnoudruzenje.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping(value ="/paypal/register")
    public ResponseEntity<?> registerPaypal(@RequestBody @Valid PaypalInfoDTO paypalInfoDTO, @RequestHeader("Authorization") String token) {
        return paymentService.registerPayment(paypalInfoDTO, token);
    }

    @PostMapping(value ="/paypal/{id}/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> executePaypalPayment(@RequestBody @Valid PaypalExecuteDTO paypalExecuteDTO, @PathVariable("id") Long orderId) {
        return paymentService.executePaypalPayment(paypalExecuteDTO, orderId);
    }

    @PostMapping(value = "paypal/{id}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelPaypalPayment(@PathVariable("id") Long orderId) {
        return paymentService.cancelPaypalPayment(orderId);
    }

    @PostMapping(value = "paypal/billingPlan", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBillingPlan(@RequestBody @Valid CreateBillingPlanDTO createBillingPlanDTO) {
        return paymentService.createBillingPlanForItem(createBillingPlanDTO);
    }

    @PostMapping(value = "paypal/subscription", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSubscription(@RequestBody CreateSubscriptionDTO createSubscriptionDTO, @RequestHeader("Authorization") String token) {
        return paymentService.createSubscription(createSubscriptionDTO, token);
    }

    @PostMapping(value = "paypal/subscription/{id}/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> executeSubscription(@RequestBody @Valid ExecuteSubscriptionDTO executeSubscriptionDTO, @PathVariable("id") Long subscriptionId) {
        return paymentService.executeSubscription(executeSubscriptionDTO, subscriptionId);
    }

    @PostMapping(value = "paypal/subscription/{id}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelSubscription(@PathVariable("id") Long subscriptionId) {
        return paymentService.cancelSubscription(subscriptionId);
    }

    @PostMapping(value ="/bank/register")
    public ResponseEntity<?> registerBank(@RequestBody @Valid BankInfoDTO bankInfoDTO, @RequestHeader("Authorization") String token) {
        return paymentService.registerPayment(bankInfoDTO, token);
    }

    @PostMapping(value ="/btc/register")
    public ResponseEntity<?> registerBtc(@RequestBody @Valid BtcInfoDTO btcInfoDTO, @RequestHeader("Authorization") String token) {
        return paymentService.registerPayment(btcInfoDTO, token);
    }

    @PostMapping(value = "/btc/transaction/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBtcTransaction(@RequestBody @Valid BtcExecuteDTO btcExecuteDTO) {
        return paymentService.updateBtcTransaction(btcExecuteDTO);
    }

    @PostMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestBody @Valid CreateOrderDTO createOrderDTO, @RequestHeader("Authorization") String token) {
        return paymentService.createOrder(createOrderDTO, token);
    }

    @GetMapping(value = "types")
    public ResponseEntity<?> getPaymentTypes(@RequestHeader("Authorization") String token) {
        return paymentService.getSellerPaymentTypes(token);
    }


}
