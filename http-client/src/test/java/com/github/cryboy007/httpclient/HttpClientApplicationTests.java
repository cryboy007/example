package com.github.cryboy007.httpclient;

import com.github.cryboy007.HttpClientApplication;
import com.github.cryboy007.httpclient.example.MTBlogExample;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HttpClientApplication.class)
@RequiredArgsConstructor
class HttpClientApplicationTests {
	private final MTBlogExample mtBlogExample;

	@Test
	public void contextLoads() {
		mtBlogExample.getBlog();
	}

}
