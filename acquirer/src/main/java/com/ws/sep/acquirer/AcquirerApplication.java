package com.ws.sep.acquirer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import util.EncryptDecrypt;
import util.HashUtil;
import util.PanBankIdUtil;

@SpringBootApplication
@RestController
@EnableScheduling
public class AcquirerApplication
{

	// mvn -Dspring-boot.run.jvmArguments="-Dport=8089 -Ddb=jj
	// -Dscript=classpath:bank2.sql" spring-boot:run
	public static void main( String[] args )
	{
		SpringApplication.run( AcquirerApplication.class, args );

		String[] text =
		{ "1111117777777777", "Pera Peric", "12", "22", "123", "2222225555555555", "Misa Misic", "12", "22", "123", "9999994444444444", "Joca Jocic", "12",
				"22", "123", "8888883333333333", "Mika Mikic", "12", "22", "123" };

		for ( String s : text )
		{
			System.err.println( s );
			System.err.println( EncryptDecrypt.encryptString( s ) );
		}

	}


	@Bean
	public CorsFilter corsFilter()
	{

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		// config.setAllowCredentials(true); // you USUALLY want this
		config.addAllowedOrigin( "*" );
		config.addAllowedHeader( "*" );
		config.addAllowedMethod( "OPTIONS" );
		config.addAllowedMethod( "HEAD" );
		config.addAllowedMethod( "GET" );
		config.addAllowedMethod( "PUT" );
		config.addAllowedMethod( "POST" );
		config.addAllowedMethod( "DELETE" );
		config.addAllowedMethod( "PATCH" );
		source.registerCorsConfiguration( "/**", config );
		return new CorsFilter( source );

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


	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();

	}


	@Bean
	public HashUtil hashUtil()
	{
		return new HashUtil();

	}

}
