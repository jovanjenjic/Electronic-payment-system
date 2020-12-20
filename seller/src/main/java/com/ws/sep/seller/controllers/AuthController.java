package com.ws.sep.seller.controllers;

import javax.validation.Valid;


import com.ws.sep.seller.payload.LoginRequest;
import com.ws.sep.seller.payload.SignUpRequest;
import com.ws.sep.seller.services.AuthService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( value = "/api/auth" )
public class AuthController
{

    @Autowired
    private AuthService authService;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping( "/signup" )
    public ResponseEntity< ? > register( @Valid @RequestBody SignUpRequest request ) throws Exception
    {
        logger.info("INFO - /signup");
        return this.authService.registerUser( request );
    }


    @PostMapping( "/signin" )
    public ResponseEntity< ? > signin( @Valid @RequestBody LoginRequest login )
    {
        logger.info("INFO - /signin");
        return this.authService.authenticateUser( login );
    }
}
