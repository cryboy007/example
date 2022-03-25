package com.github.crybooy007.mq;

import com.github.crybooy007.mq.enums.MqSceneEnum;
import com.github.crybooy007.mq.rabbitmq.producer.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MqApplication.class)
@RequiredArgsConstructor
class MqApplicationTests {
	private final MessageProvider messageProvider;


	@Test
	public void contextLoads(){
		messageProvider.onMessage(MqSceneEnum.TEST_ONE,"你好吗");
	}

}
