package com.ws.sep.seller.services;

import java.time.LocalDateTime;
import java.util.Collections;


import com.ws.sep.seller.models.Role;
import com.ws.sep.seller.models.RoleName;
import com.ws.sep.seller.models.Seller;
import com.ws.sep.seller.payload.ApiResponse;
import com.ws.sep.seller.payload.JwtResponse;
import com.ws.sep.seller.payload.LoginRequest;
import com.ws.sep.seller.payload.SignUpRequest;
import com.ws.sep.seller.repositories.IRoleRepository;
import com.ws.sep.seller.repositories.ISellerRepository;
import com.ws.sep.seller.security.JwtTokenProvider;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ISellerRepository iSellerRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider provider;

    Logger logger = LoggerFactory.getLogger( AuthService.class );

    public ResponseEntity< JwtResponse > authenticateUser( @RequestBody LoginRequest loginRequest )
    {
        Authentication authentication =
                authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( loginRequest.getEmail(), loginRequest.getPassword() ) );

        SecurityContextHolder.getContext().setAuthentication( authentication );

        String jwt = provider.generateToken( authentication );

        logger.info( "Logged in to the system." );
        return ResponseEntity.ok( new JwtResponse( jwt ) );

    }


    public ResponseEntity< ApiResponse > registerUser( @RequestBody SignUpRequest signUpRequest ) throws Exception
    {
        if ( this.iSellerRepository.existsByEmail( signUpRequest.getEmail() ) )
        {
            // TODO: logger
            logger.warn( "Register : Email is already in use." );
            return new ResponseEntity< ApiResponse >( new ApiResponse( "email is already in use", false ), HttpStatus.BAD_REQUEST );
        }

        if ( this.iSellerRepository.existsByPib( signUpRequest.getPib() ) )
        {
            // TODO: logger
            logger.warn( "Register : PIB is already in use." );
            return new ResponseEntity< ApiResponse >( new ApiResponse( "pib is already in use", false ), HttpStatus.BAD_REQUEST );
        }

        Seller seller = new Seller();

        seller.setEmail( signUpRequest.getEmail() );
        seller.setPassword( passwordEncoder.encode( signUpRequest.getPassword() ) );
        seller.setPib( signUpRequest.getPib() );
        seller.setCreatedAt( LocalDateTime.now() );

        Role userRole = this.iRoleRepository.findByName( RoleName.ROLE_USER ).orElseThrow( () -> new Exception( "User Role not set." ) );

        seller.setRoles( Collections.singleton( userRole ) );

        iSellerRepository.save( seller );

        ResponseEntity< JwtResponse > authenticateUser = this.authenticateUser( new LoginRequest( signUpRequest.getEmail(), signUpRequest.getPassword() ) );

        JwtResponse body = authenticateUser.getBody();

        logger.info( "Register : Successful registration in the system." );
        return new ResponseEntity< ApiResponse >( new ApiResponse( body.getAccessToken(), true ), HttpStatus.CREATED );

    }

}
