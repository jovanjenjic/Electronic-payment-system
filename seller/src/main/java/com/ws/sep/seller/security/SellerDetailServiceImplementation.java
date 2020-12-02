package com.ws.sep.seller.security;

import com.ws.sep.seller.models.Seller;
import com.ws.sep.seller.repositories.ISellerRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SellerDetailServiceImplementation implements UserDetailsService
{

    @Autowired
    ISellerRepository ISellerRepository;

    @Override
    public UserDetails loadUserByUsername( String email ) throws UsernameNotFoundException
    {
        Seller seller = this.ISellerRepository.findByEmail( email )
                .orElseThrow( () -> new UsernameNotFoundException( "Seller with email [" + email + "] not found!" ) );

        return SellerPrincipal.create( seller );

    }


    public UserDetails loadSellerById( Long id )
    {
        Seller seller = this.ISellerRepository.findById( id ).orElseThrow( () -> new UsernameNotFoundException( "Seller with id [" + id + "] not found!" ) );

        return SellerPrincipal.create( seller );

    }

}
