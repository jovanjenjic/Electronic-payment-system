package com.ws.sep.literalnoudruzenje.controllers;

import com.ws.sep.literalnoudruzenje.dto.PaypalInfoDTO;
import com.ws.sep.literalnoudruzenje.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return paymentService.registerPaypal(paypalInfoDTO, token);
    }

    @PostMapping(value ="/bank/register")
    public ResponseEntity<?> registerBank() {
        return null;
    }

    @PostMapping(value ="/btc/register")
    public ResponseEntity<?> registerBtc() {
        return null;
    }

}
