package com.ws.sep.bankservice.controllers;

import com.ws.sep.bankservice.proxy.IFeignClientProxy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/test3" )
public class BankServiceController
{

    @Autowired
    private IFeignClientProxy proxy;

    @GetMapping( value = "/" )
    public String test()
    {
        System.out.println("AAAAAA");
        return "OKay";
    }



}
