package com.ws.sep.literalnoudruzenje.controllers;

import com.ws.sep.literalnoudruzenje.config.UserPrincipalService;
import com.ws.sep.literalnoudruzenje.dto.LoginDTO;
import com.ws.sep.literalnoudruzenje.dto.RegisterDTO;
import com.ws.sep.literalnoudruzenje.dto.UserResponseDTO;
import com.ws.sep.literalnoudruzenje.exceptions.SimpleException;
import com.ws.sep.literalnoudruzenje.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    UserPrincipalService userPrincipalService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;


    @PostMapping("/register")
    public UserResponseDTO registerUser(@RequestBody @Valid RegisterDTO registerData) throws SimpleException {
        return userService.registerUser(registerData);
    }

    @PostMapping("/login")
    public ResponseEntity<?> registerUser(@RequestBody @Valid LoginDTO loginData) throws SimpleException {
        return userService.loginUser(loginData);
    }

}
