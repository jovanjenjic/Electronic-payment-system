package com.ws.sep.paypalservice.controllers;

import com.ws.sep.paypalservice.dto.Field;
import com.ws.sep.paypalservice.dto.SellerInfoDTO;
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

        sellerInfoService.addPayment(sellerInfoDTO, token);

        HashMap<String, String> retMessage = new HashMap<>();
        retMessage.put("status", "success");
        retMessage.put("message", "payment method added successfully!");

        return new ResponseEntity<>(retMessage, HttpStatus.CREATED);
    }

}
