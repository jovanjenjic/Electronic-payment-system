package com.ws.sep.acquirer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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

	@Bean
	public CorsFilter corsFilter() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		//config.setAllowCredentials(true); // you USUALLY want this
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("HEAD");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("PATCH");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
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
