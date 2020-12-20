package utils;

import java.util.function.Function;


import org.springframework.beans.factory.annotation.Value;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtil
{

    @Value( "${jwt.secret}" )
    private String SECRET_KEY;

    public < T > T extractClaim( String token, Function< Claims, T > claimsResolver )
    {
        final Claims claims = extractAllClaims( token );
        return claimsResolver.apply( claims );

    }


    private Claims extractAllClaims( String token )
    {
        return Jwts.parser().setSigningKey( SECRET_KEY ).parseClaimsJws( token ).getBody();

    }


    public Long extractSellerId( String token )
    {
        return Long.valueOf( extractClaim( token.substring( 7 ), Claims::getSubject ) );

    }

}
