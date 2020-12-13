package com.ws.sep.bankservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/test1" )
public class BankServiceController
{

    @GetMapping( value = "/" )
    public String messageBank()
    {
        return "Bank service";
    }

}
