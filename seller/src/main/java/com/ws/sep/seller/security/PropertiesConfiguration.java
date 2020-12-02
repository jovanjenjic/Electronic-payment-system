package com.ws.sep.seller.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


import lombok.Getter;

@Getter
@Component
@ConfigurationProperties( "sellers" )
public class PropertiesConfiguration
{

    private String jwtSecret;

    private Long jwtExpirationInMs;

}
