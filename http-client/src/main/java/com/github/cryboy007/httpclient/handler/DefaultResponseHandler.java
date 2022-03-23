package com.github.cryboy007.httpclient.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import com.google.common.io.CharStreams;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 *@ClassName DefaultResponseHandler
 *@Author tao.he
 *@Since 2022/3/24 0:10
 */
@Slf4j
public class DefaultResponseHandler implements ResponseErrorHandler {
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return response.getStatusCode().isError();
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		InputStream inputStream = response.getBody();
		InputStreamReader isr = new InputStreamReader(inputStream);
		String errorMsg = CharStreams.toString(isr);
		log.error("详细报错信息{}", errorMsg);
	}

	@Override
	public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
		ResponseErrorHandler.super.handleError(url, method, response);
	}
}
