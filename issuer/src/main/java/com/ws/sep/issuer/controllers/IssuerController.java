package com.ws.sep.issuer.controllers;

import com.ws.sep.issuer.dtos.PccRequest;
import com.ws.sep.issuer.services.IssuerService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IssuerController
{

    @Autowired
    private IssuerService issuerService;

    @PostMapping("/check")
    public ResponseEntity< ? > check( @RequestBody PccRequest request )
    {
        return this.issuerService.check( request );

    }

}
