package com.github.cryboy007.httpclient.initialize;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *@ClassName StartInitBean
 *@Author tao.he
 *@Since 2022/3/24 0:36
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class StartInitBean implements CommandLineRunner {
	private final RestTemplate restTemplate;



	@Override
	public void run(String... args) throws Exception {
		new Thread(() -> {
			ResponseEntity<String> forEntity = restTemplate.getForEntity("https://znsd.top:3007", String.class);
			log.info("执行完毕");
		}).start();
	}
}
