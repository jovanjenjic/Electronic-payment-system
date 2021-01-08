package com.ws.sep.literalnoudruzenje.controllers;

import com.ws.sep.literalnoudruzenje.dto.BankInfoDTO;
import com.ws.sep.literalnoudruzenje.dto.BtcInfoDTO;
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
        return paymentService.registerPayment(paypalInfoDTO, token);
    }

    @PostMapping(value ="/bank/register")
    public ResponseEntity<?> registerBank(@RequestBody @Valid BankInfoDTO bankInfoDTO, @RequestHeader("Authorization") String token) {
        return paymentService.registerPayment(bankInfoDTO, token);
    }

    @PostMapping(value ="/btc/register")
    public ResponseEntity<?> registerBtc(@RequestBody @Valid BtcInfoDTO btcInfoDTO, @RequestHeader("Authorization") String token) {
        return paymentService.registerPayment(btcInfoDTO, token);
    }

}
