package com.ws.sep.paypalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients( "com.ws.sep.paypalservice" )
@EnableDiscoveryClient
public class PayPalServiceApplication
{

	public static void main( String[] args )
	{
		SpringApplication.run( PayPalServiceApplication.class, args );

	}

}
