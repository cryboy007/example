package com.github.cryboy007.httpclient.config;

import java.util.concurrent.TimeUnit;

import com.github.cryboy007.httpclient.handler.DefaultResponseHandler;
import com.github.cryboy007.httpclient.interceptor.TokenClientHttpRequestInterceptor;
import com.overzealous.remark.Remark;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *@ClassName RestConfig
 *@Author tao.he
 *@Since 2022/3/23 23:54
 */
@Component
@Slf4j
public class RestConfig {
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		// 设置连接池参数，最大空闲连接数200，空闲连接存活时间10s
		ConnectionPool connectionPool = new ConnectionPool(1000, 10, TimeUnit.SECONDS);
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.retryOnConnectionFailure(false).connectionPool(connectionPool)
				.connectTimeout(6, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.MINUTES)
				.writeTimeout(30, TimeUnit.SECONDS)
				.build();
		OkHttp3ClientHttpRequestFactory clientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
		restTemplate.setRequestFactory(clientHttpRequestFactory);
		restTemplate.setErrorHandler(new DefaultResponseHandler());
		restTemplate.getInterceptors().add(new TokenClientHttpRequestInterceptor());
		return restTemplate;
	}

	@Bean
	private Remark createRemarkBean() {
		return new Remark();
	}

	@Bean
	private ClassPathXmlApplicationContext pathXmlApplicationContext() {
		return new ClassPathXmlApplicationContext();
	}

}
