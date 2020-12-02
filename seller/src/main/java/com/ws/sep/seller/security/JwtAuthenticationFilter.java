package com.ws.sep.seller.security;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter
{

    @Autowired
    private JwtTokenProvider provider;

    @Autowired
    private SellerDetailServiceImplementation sellerDetailServiceImplementation;

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException
    {
        try
        {
            String jwt = getJwtFromRequest( request );

            if ( StringUtils.hasText( jwt ) && provider.validateToken( jwt ) )
            {
                Long sellerId = provider.getUserIdFromJWT( jwt );

                UserDetails sellerDetails = sellerDetailServiceImplementation.loadSellerById( sellerId );

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken( sellerDetails, null, sellerDetails.getAuthorities() );

                authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );

                SecurityContextHolder.getContext().setAuthentication( authenticationToken );

            }

        }
        catch ( Exception e )
        {
            System.err.println( e );

        }
        filterChain.doFilter( request, response );

    }


    private String getJwtFromRequest( HttpServletRequest request )
    {
        String bearerToken = request.getHeader( "Authorization" );
        if ( StringUtils.hasText( bearerToken ) && bearerToken.startsWith( "Bearer " ) )
        {
            return bearerToken.substring( 7, bearerToken.length() );
        }
        return null;

    }

}
