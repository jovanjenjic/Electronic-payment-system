package com.ws.sep.acquirer.controllers;

import com.ws.sep.acquirer.dtos.ApiResponse;
import com.ws.sep.acquirer.dtos.CreateClientRequest;
import com.ws.sep.acquirer.dtos.PaymentRequest;
import com.ws.sep.acquirer.services.ClientService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( value = "/api" )
public class ClientController
{

    @Autowired
    private ClientService clientService;

    @PostMapping( value = "/" )
    public ResponseEntity< ApiResponse > createClient( @RequestBody CreateClientRequest request )
    {
        return this.clientService.createClientAccount( request );

    }


    @PostMapping( "/acquirer/generate" )
    public ResponseEntity< ? > generatePaymentResponse( @RequestBody PaymentRequest request )
    {
        return this.clientService.generatePaymentResponse( request );

    }

}
