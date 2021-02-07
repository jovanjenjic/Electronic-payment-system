package com.ws.sep.bitcoinservice.controllers;

import com.ws.sep.bitcoinservice.dto.*;
import com.ws.sep.bitcoinservice.services.CryptocurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.FileAlreadyExistsException;
import java.util.Map;

@RestController
@RequestMapping( "/api" )
public class CryptocurrencyController {

    Logger logger = LoggerFactory.getLogger(CryptocurrencyController.class);

    @Autowired
    CryptocurrencyService cryptocurrencyService;

    @GetMapping( value = "/form" )
    public ResponseEntity<?> getForm() {
        logger.info("INFO - /form");
        return new ResponseEntity<>(cryptocurrencyService.getFormForPayment(), HttpStatus.OK);
    }

    @PostMapping( value = "/addPayment" )
    public ResponseEntity<?> registerPayment(@RequestBody SellerInfoDTO sellerInfoDTO, @RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        logger.info("INFO - /registerPayment");
        return new ResponseEntity<>(cryptocurrencyService.registerPayment(sellerInfoDTO, token), HttpStatus.OK);
    }

    @PostMapping( value = "/createOrder" )
    public ResponseEntity<?> createOrder(@RequestBody TransactionDetailsDTO transactionDetailsDTO, @RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        logger.info("INFO - /createOrder");
        return new ResponseEntity<>(cryptocurrencyService.createOrder(transactionDetailsDTO, token), HttpStatus.OK);
    }

    @PostMapping( value = "/checkoutOrder" )
    public ResponseEntity<?> checkoutOrder(@RequestBody CheckoutOrderDTO checkoutOrderDTO, @RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        logger.info("INFO - /checkoutOrder");
        return new ResponseEntity<>(cryptocurrencyService.checkoutOrder(checkoutOrderDTO, token), HttpStatus.OK);
    }

    @GetMapping( value = "/getOrder" )
    public ResponseEntity<?> getOrder(@RequestBody OrderIdDTO orderIdDTO, @RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        logger.info("INFO - /getOrder");
        return new ResponseEntity<>(cryptocurrencyService.getOrder(orderIdDTO, token), HttpStatus.OK);
    }

    @GetMapping( value = "/listAllOrderOfSeller" )
    public ResponseEntity<?> listAllOrderOfSeller(@RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        logger.info("INFO - /listAllOrderOfSeller");
        return new ResponseEntity<>(cryptocurrencyService.listAllPaymentOfSeller(token), HttpStatus.OK);
    }

    @PostMapping( value = "/setStateOfTransaction" )
    public ResponseEntity<?> setStateOfTransaction(@RequestBody SuccessCancelDTO successCancelDTO, @RequestHeader("Authorization") String token) throws FileAlreadyExistsException {
        logger.info("INFO - /setStateOfTransaction");
        return new ResponseEntity<>(cryptocurrencyService.setStateOnSuccessOrCancel(successCancelDTO, token), HttpStatus.OK);
    }

    @PutMapping( value = "/addPayment" )
    public ResponseEntity<?> setApiKey(@RequestBody SetApiKeyDTO apiKey, @RequestHeader("Authorization") String token) {
        logger.info("INFO - /setApiKey");
        return new ResponseEntity<>(cryptocurrencyService.setApiKey(apiKey, token), HttpStatus.OK);
    }

    @GetMapping(value = "/getTransactions")
    public ResponseEntity<?> getTransactions(@RequestParam Map<String, String> params, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(cryptocurrencyService.getTransactions(params.getOrDefault("state", ""), token), HttpStatus.OK);
    }

}
