package com.dellemc.dam.assetmetadata.config;

import lombok.Data;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;

@Configuration
@ConfigurationProperties(prefix = "assetmetadata")
@Data
public class AppConfig {

	@Value("${assetmetadata.onedamcoveoapi}")
	private String onedamcoveoapi;
	@Value("${assetmetadata.coveousername}")
	private String coveousername;
	@Value("${assetmetadata.coveopassword}")
	private String coveopassword;
	@Value("${assetmetadata.domainonedam}")
	private String domainonedam;
	@Value("${assetmetadata.sitemaplimit}")
	private int sitemaplimit;

	@Bean({ "CoveoBean" })
	public RestTemplate getCoveoRestTemplate() {
		return new RestTemplate(getClientHttpRequestFactory(onedamcoveoapi,coveousername, coveopassword));
	}

	@Bean(name = "oneDamCoveositemapService")
	public CoveositemapService getOneDamCoveositemapService() {
		return new CoveositemapService(domainonedam, sitemaplimit, onedamcoveoapi);
	}

	private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(String api,String userName, String password) {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient((org.apache.http.client.HttpClient)httpClient(api.trim(),userName.trim(), password.trim()));
		return clientHttpRequestFactory;
	}

	private HttpClient httpClient(String api , String username, String password) {
		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		URL url;
		try {
			url = new URL(api);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		credentialsProvider.setCredentials(new AuthScope(new HttpHost(url.getHost())), new UsernamePasswordCredentials(username, password.toCharArray()));
		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
		return client;
	}

}