package com.ws.sep.bankservice.controllers;

import com.ws.sep.bankservice.dtos.ApiResponse;
import com.ws.sep.bankservice.dtos.CreateMerchantRequest;
import com.ws.sep.bankservice.dtos.OrderDTO;
import com.ws.sep.bankservice.services.MerchantService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( value = "/api/merchant" )
public class MerchantController
{

    @Autowired
    private MerchantService merchantService;

    @PostMapping( value = "/" )
    public ResponseEntity< ApiResponse > createMerchant( @RequestBody CreateMerchantRequest request, @RequestHeader( "Authorization" ) String token )
    {
        return this.merchantService.createMerchant( request, token );

    }


    @PostMapping( "/create" )
    public ResponseEntity< ? > retrieveUrlAndId( @RequestBody OrderDTO order, @RequestHeader( "Authorization" ) String token )
    {
        return this.merchantService.retrieveUrlAndId( order, token );

    }

}
