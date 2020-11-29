package com.ws.sep.netflixzuulapigatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl.EurekaJerseyClientBuilder;


@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class NetflixZuulApiGatewayServerApplication
{

	@Bean
	public DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs() throws NoSuchAlgorithmException {
		DiscoveryClient.DiscoveryClientOptionalArgs args = new DiscoveryClient.DiscoveryClientOptionalArgs();
		System.setProperty("javax.net.ssl.keyStore", "src/main/resources/keystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "password");
		System.setProperty("javax.net.ssl.trustStore", "src/main/resources/keystore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");
		EurekaJerseyClientBuilder builder = new EurekaJerseyClientBuilder();
		builder.withClientName("zuul");
		builder.withSystemSSLConfiguration();
		builder.withMaxTotalConnections(10);
		builder.withMaxConnectionsPerHost(10);
		args.setEurekaJerseyClient(builder.build());
		return args;
	}

	@Bean
	public CloseableHttpClient zuulHttpClient() throws Throwable {
		final KeyStore ks = KeyStore.getInstance("jks");
		ClassPathResource classPathResource = new ClassPathResource("keystore.jks");
		InputStream inputStream = classPathResource.getInputStream();
		ks.load(inputStream, "password".toCharArray());


		final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(ks, "password".toCharArray());


		final KeyStore ts = KeyStore.getInstance("jks");
		ClassPathResource classPathResource2 = new ClassPathResource("keystore.jks");
		InputStream inputStream2 = classPathResource2.getInputStream();
		ks.load(inputStream2, "password".toCharArray());

		final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(ts);

		final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(ts, new TrustSelfSignedStrategy()).loadKeyMaterial(ks, "password".toCharArray()).build();
		final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setSSLContext(sslContext);
		return httpClientBuilder.build();
	}


	public static void main( String[] args )
	{
		SpringApplication.run( NetflixZuulApiGatewayServerApplication.class, args );

	}

}
