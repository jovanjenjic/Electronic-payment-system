package com.ws.sep.acquirer.controllers;

import com.ws.sep.acquirer.dtos.ApiResponse;
import com.ws.sep.acquirer.dtos.CreateClientRequest;
import com.ws.sep.acquirer.dtos.CreditCardInfo;
import com.ws.sep.acquirer.dtos.PaymentRequest;
import com.ws.sep.acquirer.services.ClientService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping( "/" )
    public String get()
    {
        return "ok";

    }


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


    @GetMapping( "/acquirer/form/{id}" )
    public ResponseEntity< ? > getForm( @PathVariable Long id )
    {
        return this.clientService.getForm();

    }


    @PostMapping( "/acquirer/form/{id}" )
    public ResponseEntity< ? > startPayment( @RequestBody CreditCardInfo card, @PathVariable Long id )
    {
        return this.clientService.startPayment( card, id );

    }

}
