package com.ws.sep.seller.controllers;

import com.ws.sep.seller.payload.PaymentTypeRequest;
import com.ws.sep.seller.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( value = "/api/payment" )
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping(value = "")
    public ResponseEntity<?> updateSellerPaymentTypes(@RequestBody PaymentTypeRequest paymentTypeRequest, @RequestHeader("Authorization") String token) {

        boolean success = paymentService.addPaymentType(paymentTypeRequest, token);

        return success ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
