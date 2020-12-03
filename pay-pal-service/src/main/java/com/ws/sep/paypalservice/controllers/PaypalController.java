package com.ws.sep.paypalservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/test1" )
public class PaypalController
{

    @GetMapping( value = "/" )
    public String messagePaypal()
    {
        return "Paypal service";
    }

}
