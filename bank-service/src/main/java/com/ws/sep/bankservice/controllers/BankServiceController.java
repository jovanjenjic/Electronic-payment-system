package com.ws.sep.bankservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import utils.EncryptionDecryption;

@RestController
@RequestMapping( "/test1" )
public class BankServiceController
{

    @GetMapping( value = "/" )
    public String messageBank()
    {

        String text = "123456";

        try
        {
            String encryptString = EncryptionDecryption.encryptString( text );
            String decryptString = EncryptionDecryption.decryptString( encryptString );

            System.err.println( encryptString );
            System.err.println( decryptString );
            System.err.println( encryptString.equals( decryptString ) );

        }
        catch ( Exception e )
        {
            System.err.println( "ajoj" );
        }

        return "Bank service";

    }

}
