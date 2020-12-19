package com.ws.sep.pcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import util.PanBankIdUtil;

@SpringBootApplication
public class PccApplication
{

	public static void main( String[] args )
	{
		SpringApplication.run( PccApplication.class, args );

	}


	@Bean
	public PanBankIdUtil panBankIdUtilObject()
	{
		return new PanBankIdUtil();

	}

}
