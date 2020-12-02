package com.ws.sep.seller.security;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ws.sep.seller.models.Seller;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerPrincipal implements UserDetails
{

    /**
     *
     */
    private static final long serialVersionUID = 1175670991961487821L;

    private Long id;

    private String email;

    private String username;

    private String name;

    private Long pib;

    private LocalDateTime createdAt;

    @JsonIgnore
    private String password;

    private Collection< ? extends GrantedAuthority > authorities;

    public SellerPrincipal( Long id, String email, String password, Collection< ? extends GrantedAuthority > authorities )
    {
        this.id = id;
        this.name = email;
        this.username = email;
        this.email = email;
        this.password = password;
        this.authorities = authorities;

    }


    public static SellerPrincipal create( Seller seller )
    {
        List< GrantedAuthority > authorities =
                seller.getRoles().stream().map( role -> new SimpleGrantedAuthority( role.getName().name() ) ).collect( Collectors.toList() );

        return new SellerPrincipal( seller.getId(), seller.getEmail(), seller.getPassword(), authorities );

    }


    @Override
    public Collection< ? extends GrantedAuthority > getAuthorities()
    {

        return this.authorities;

    }


    @Override
    public String getPassword()
    {
        return this.password;

    }


    @Override
    public String getUsername()
    {
        return this.email;

    }


    @Override
    public boolean isAccountNonExpired()
    {
        return true;

    }


    @Override
    public boolean isAccountNonLocked()
    {
        return true;

    }


    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;

    }


    @Override
    public boolean isEnabled()
    {
        // TODO Auto-generated method stub
        return true;

    }

}
