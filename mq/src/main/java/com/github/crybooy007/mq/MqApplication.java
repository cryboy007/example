package com.github.crybooy007.mq;

import java.util.concurrent.TimeUnit;

import com.github.crybooy007.mq.enums.MqSceneEnum;
import com.github.crybooy007.mq.rabbitmq.producer.MessageProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
public class MqApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqApplication.class, args);
	}

	@Autowired
	@Lazy
	public void contextLoads(MessageProvider messageProvider) throws InterruptedException {
		messageProvider.onMessage(MqSceneEnum.TEST_ONE,"你好吗");
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(20);
				messageProvider.onMessage(MqSceneEnum.TEST_ONE,"我不好");
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

}
