package com.ws.sep.acquirer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import util.PanBankIdUtil;

@SpringBootApplication
@RestController
public class AcquirerApplication
{

	// mvn -Dspring-boot.run.jvmArguments="-Dport=8089 -Ddb=jj
	// -Dscript=classpath:bank2.sql" spring-boot:run
	public static void main( String[] args )
	{
		SpringApplication.run( AcquirerApplication.class, args );

	}


	@GetMapping( "/" )
	public String get()
	{
		return "ok";

	}

	@Bean
	public PanBankIdUtil panBankIdUtilObject()
	{
		return new PanBankIdUtil();

	}

}
