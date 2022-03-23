package com.github.cryboy007.httpclient.interceptor;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 *@ClassName TokenClientHttpRequestInterceptor
 *@Author tao.he
 *@Since 2022/3/24 0:20
 */
@Slf4j
public class TokenClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	private String token;

	public TokenClientHttpRequestInterceptor(String token) {
		this.token = token;
	}
	public TokenClientHttpRequestInterceptor() {
	}


	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpHeaders headers = request.getHeaders();
		if (!headers.containsKey("token")) {
			headers.set("token",this.token == null ?  "" : this.token);
		}
		return execution.execute(request, body);
	}
}
