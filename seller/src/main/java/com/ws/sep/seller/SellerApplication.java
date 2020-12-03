package com.ws.sep.seller;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;


import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl;


import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@ComponentScan( "com.ws.sep.seller" )
public class SellerApplication
{

	public static void main( String[] args )
	{
		SpringApplication.run( SellerApplication.class, args );

	}


	@GetMapping( value = "/" )
	public String getMethodName()
	{
		return "OKOKOKOKOKO";

	}


	@Bean
	public DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs() throws NoSuchAlgorithmException
	{
		DiscoveryClient.DiscoveryClientOptionalArgs args = new DiscoveryClient.DiscoveryClientOptionalArgs();
		System.setProperty( "javax.net.ssl.keyStore", "src/main/resources/keystore.jks" );
		System.setProperty( "javax.net.ssl.keyStorePassword", "password" );
		System.setProperty( "javax.net.ssl.trustStore", "src/main/resources/keystore.jks" );
		System.setProperty( "javax.net.ssl.trustStorePassword", "password" );
		EurekaJerseyClientImpl.EurekaJerseyClientBuilder builder = new EurekaJerseyClientImpl.EurekaJerseyClientBuilder();
		builder.withClientName( "seller" );
		builder.withSystemSSLConfiguration();
		builder.withMaxTotalConnections( 10 );
		builder.withMaxConnectionsPerHost( 10 );
		args.setEurekaJerseyClient( builder.build() );
		return args;

	}


	@Bean
	public RestTemplate getRestTemplate()
	{
		RestTemplate restTemplate = new RestTemplate();

		KeyStore keyStore;
		HttpComponentsClientHttpRequestFactory requestFactory = null;

		try
		{
			keyStore = KeyStore.getInstance( "jks" );
			ClassPathResource classPathResource = new ClassPathResource( "keystore.jks" );
			InputStream inputStream = classPathResource.getInputStream();
			keyStore.load( inputStream, "password".toCharArray() );

			SSLConnectionSocketFactory socketFactory =
					new SSLConnectionSocketFactory( new SSLContextBuilder().loadTrustMaterial( null, new TrustSelfSignedStrategy() )
							.loadKeyMaterial( keyStore, "password".toCharArray() ).build(), NoopHostnameVerifier.INSTANCE );

			HttpClient httpClient = HttpClients.custom().setSSLSocketFactory( socketFactory ).setMaxConnTotal( Integer.valueOf( 5 ) )
					.setMaxConnPerRoute( Integer.valueOf( 5 ) ).build();

			requestFactory = new HttpComponentsClientHttpRequestFactory( httpClient );
			requestFactory.setReadTimeout( Integer.valueOf( 10000 ) );
			requestFactory.setConnectTimeout( Integer.valueOf( 10000 ) );

			restTemplate.setRequestFactory( requestFactory );
		}
		catch ( Exception exception )
		{
			System.out.println( "Exception Occured while creating restTemplate " + exception );
			exception.printStackTrace();
		}
		return restTemplate;

	}

}
