package com.ws.sep.bitcoinservice.utility;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Long extractSellerId(String token) {
        return Long.valueOf(extractClaim(token, Claims::getSubject));
    }
}
