package com.ws.sep.bitcoinservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/test1" )
public class BitcoinServiceController
{

    @GetMapping( value = "/" )
    public String messageBitcoin()
    {
        return "Bitcoin service";
    }

}
