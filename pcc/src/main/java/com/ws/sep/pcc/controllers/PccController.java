package com.ws.sep.pcc.controllers;

import com.ws.sep.pcc.dtos.AcquireRequest;
import com.ws.sep.pcc.services.PccService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PccController
{

    @Autowired
    private PccService pccService;

    @PostMapping( "/check" )
    public ResponseEntity< ? > check( @RequestBody AcquireRequest request )
    {
        return this.pccService.forwardRequest( request );

    }

}
