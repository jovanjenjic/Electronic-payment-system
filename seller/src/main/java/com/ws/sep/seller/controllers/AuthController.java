package com.ws.sep.seller.controllers;

import javax.validation.Valid;


import com.ws.sep.seller.payload.LoginRequest;
import com.ws.sep.seller.payload.SignUpRequest;
import com.ws.sep.seller.services.AuthService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( value = "/api/auth" )
public class AuthController
{

    @Autowired
    private AuthService authService;

    @PostMapping( "/signup" )
    public ResponseEntity< ? > register( @Valid @RequestBody SignUpRequest request ) throws Exception
    {
        return this.authService.registerUser( request );

    }


    @PostMapping( "/signin" )
    public ResponseEntity< ? > signin( @Valid @RequestBody LoginRequest login )
    {
        return this.authService.authenticateUser( login );

    }


    @GetMapping( "/test" )
    public String test()
    {
        return "ajme mene";

    }

}
