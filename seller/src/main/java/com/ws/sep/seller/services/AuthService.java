package com.ws.sep.seller.services;

import java.time.LocalDateTime;
import java.util.Collections;


import javax.validation.Valid;


import com.ws.sep.seller.models.Role;
import com.ws.sep.seller.models.RoleName;
import com.ws.sep.seller.models.Seller;
import com.ws.sep.seller.payload.JwtResponse;
import com.ws.sep.seller.payload.LoginRequest;
import com.ws.sep.seller.payload.SignUpRequest;
import com.ws.sep.seller.repositories.IRoleRepository;
import com.ws.sep.seller.repositories.ISellerRepository;
import com.ws.sep.seller.security.JwtTokenProvider;


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

    public ResponseEntity< ? > authenticateUser( @RequestBody LoginRequest loginRequest )
    {

        Authentication authentication =
                authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( loginRequest.getEmail(), loginRequest.getPassword() ) );

        SecurityContextHolder.getContext().setAuthentication( authentication );

        String jwt = provider.generateToken( authentication );
        return ResponseEntity.ok( new JwtResponse( jwt ) );

    }


    public ResponseEntity< ? > registerUser( @Valid @RequestBody SignUpRequest signUpRequest ) throws Exception
    {

        // Creating user's account
        Seller user = new Seller();

        user.setEmail( signUpRequest.getEmail() );

        user.setPassword( passwordEncoder.encode( signUpRequest.getPassword() ) );

        user.setPib( 99999989L );
        user.setCreatedAt( LocalDateTime.now() );
        Role userRole = iRoleRepository.findByName( RoleName.ROLE_ADMIN ).orElseThrow( () -> new Exception( "Ajoj" ) );

        user.setRoles( Collections.singleton( userRole ) );

        Seller result = iSellerRepository.save( user );

        return new ResponseEntity< String >( "ok", HttpStatus.CREATED );

    }

}
