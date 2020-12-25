package com.ws.sep.acquirer;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


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

		try
		{
			// extracted( "1111117777777777fdsaqwreiqrwyi24" );

			// extracted( "1" );

			// extracted( "1111117777777777" );
			// extracted( "468fadsfdsfdsass" );

			// extracted( "111777" );
			// extracted( "ASF2s4" );

			// extracted( "12" );
			// extracted( "1a" );

			for ( int i = 0; i < 10000; i++ )
			{
				SecretKey secretKey = EncryptDecrypt.generateKey( 256 );
				String keyString = Base64.getEncoder().encodeToString( secretKey.getEncoded() );

				System.err.print( keyString.length() + " " );

			}

			System.err.println( "____________________________________________________" );
			System.err.println( "____________________________________________________" );

			for ( int i = 0; i < 10000; i++ )
			{
				IvParameterSpec generateIv = EncryptDecrypt.generateIv();
				String IvParameterSpecsString = Base64.getEncoder().encodeToString( generateIv.getIV() );

				System.err.print( IvParameterSpecsString.length() + " " );

			}

		}
		catch (

		Exception e )
		{
			// TODO: handle exception
		}

	}


	private static void extracted( String n ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException
	{
		System.err.println( "*********************************************" );
		String pan = n;

		SecretKey secretKey = EncryptDecrypt.generateKey( 256 );
		IvParameterSpec ivParameterSpec = EncryptDecrypt.generateIv();
		String algorithm = "AES/CBC/PKCS5Padding";
		String encrypted1 = EncryptDecrypt.encrypt( algorithm, pan, secretKey, ivParameterSpec );
		System.err.println( "ecnrypted: " + encrypted1 );
		IvParameterSpec generateIv = EncryptDecrypt.generateIv( ivParameterSpec.getIV() );
		System.err.println( "iv1: " + Base64.getEncoder().encodeToString( generateIv.getIV() ) );

		String decrypt1 = EncryptDecrypt.decrypt( algorithm, encrypted1, secretKey, generateIv );
		System.err.println( "decry: " + decrypt1 );

		System.err.println( "encrypted length: " + encrypted1.length() );

		System.err.println( "iv length" + Base64.getEncoder().encodeToString( generateIv.getIV() ).length() );

		System.err.println( "sec key len" + secretKey.toString().length() );

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
