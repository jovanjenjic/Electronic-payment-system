package com.ws.sep.seller.security;

import java.util.Date;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider
{

    private String jWTSuperSecretKey = "JWTSuperSecretKey";

    private Long jwtExpirationInMs = 3600000L;

    // @Autowired
    // private PropertiesConfiguration propertiesConfiguration;

    public String generateToken( Authentication authentication )
    {
        SellerPrincipal sellerPrincipal = ( SellerPrincipal ) authentication.getPrincipal();

        Date now = new Date();
        Date expirationDate = new Date( now.getTime() + jwtExpirationInMs );

        return Jwts.builder().setSubject( Long.toString( sellerPrincipal.getId() ) ).setIssuedAt( now ).setExpiration( expirationDate )
                .signWith( SignatureAlgorithm.HS512, jWTSuperSecretKey ).compact();

    }


    public Long getUserIdFromJWT( String token )
    {
        Claims claims = Jwts.parser().setSigningKey( jWTSuperSecretKey ).parseClaimsJws( token ).getBody();

        return Long.parseLong( claims.getSubject() );

    }


    public boolean validateToken( String authToken )
    {

        try
        {
            Jwts.parser().setSigningKey( jWTSuperSecretKey ).parseClaimsJws( authToken );
            return true;
        }
        catch ( SignatureException ex )
        {
            // TODO: add logging
        }
        catch ( MalformedJwtException ex )
        {
            // TODO: add logging
        }
        catch ( ExpiredJwtException ex )
        {
            // TODO: add logging
        }
        catch ( UnsupportedJwtException ex )
        {
            // TODO: add logging
        }
        catch ( IllegalArgumentException ex )
        {
            // TODO: add logging
        }
        return false;

    }

}
