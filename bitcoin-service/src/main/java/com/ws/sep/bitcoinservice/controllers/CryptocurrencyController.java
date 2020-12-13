package com.ws.sep.bitcoinservice.controllers;

import com.ws.sep.bitcoinservice.dto.*;
import com.ws.sep.bitcoinservice.services.CryptocurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.FileAlreadyExistsException;

@RestController
@RequestMapping( "/api" )
public class CryptocurrencyController {

    @Autowired
    CryptocurrencyService cryptocurrencyService;

    @GetMapping( value = "/getForm" )
    public ResponseEntity<?> getForm() {
        return new ResponseEntity<>(cryptocurrencyService.getFormForPayment(), HttpStatus.OK);
    }

    @PostMapping( value = "/callback" )
    public ResponseEntity<?> callback(@RequestBody CallbackDTO callbackDTO) {
        return new ResponseEntity<>(cryptocurrencyService.callback(callbackDTO), HttpStatus.OK);
    }

    @PostMapping( value = "/registerPayment" )
    public ResponseEntity<?> registerPayment(@RequestBody SellerInfoDTO sellerInfoDTO, @RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        return new ResponseEntity<>(cryptocurrencyService.registerPayment(sellerInfoDTO, token), HttpStatus.OK);
    }

    @PostMapping( value = "/createOrder" )
    public ResponseEntity<?> createOrder(@RequestBody TransactionDetailsDTO transactionDetailsDTO, @RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        return new ResponseEntity<>(cryptocurrencyService.createOrder(transactionDetailsDTO, token), HttpStatus.OK);
    }

    @PostMapping( value = "/checkoutOrder" )
    public ResponseEntity<?> checkoutOrder(@RequestBody CheckoutOrderDTO checkoutOrderDTO, @RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        return new ResponseEntity<>(cryptocurrencyService.checkoutOrder(checkoutOrderDTO, token), HttpStatus.OK);
    }

    @GetMapping( value = "/getOrder" )
    public ResponseEntity<?> getOrder(@RequestBody OrderIdDTO orderIdDTO, @RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        return new ResponseEntity<>(cryptocurrencyService.getOrder(orderIdDTO, token), HttpStatus.OK);
    }

    @GetMapping( value = "/listAllOrderOfSeller" )
    public ResponseEntity<?> listAllOrderOfSeller(@RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        return new ResponseEntity<>(cryptocurrencyService.listAllPaymentOfSeller(token), HttpStatus.OK);
    }

    @PostMapping( value = "/setStateOfTransaction" )
    public ResponseEntity<?> setStateOfTransaction(@RequestBody SuccessCancelDTO successCancelDTO, @RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        return new ResponseEntity<>(cryptocurrencyService.setStateOnSuccessOrCancel(successCancelDTO, token), HttpStatus.OK);
    }
}
