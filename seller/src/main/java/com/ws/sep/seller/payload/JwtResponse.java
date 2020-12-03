package com.ws.sep.seller.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse
{

    private String accessToken;

    private String tokenType = "Bearer";

    public JwtResponse( String token )
    {
        this.accessToken = token;

    }

}
