package com.ws.sep.bankservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients( "com.ws.sep.bankservice" )
@EnableDiscoveryClient
public class BankServiceApplication
{

	public static void main( String[] args )
	{
		SpringApplication.run( BankServiceApplication.class, args );

	}

}
