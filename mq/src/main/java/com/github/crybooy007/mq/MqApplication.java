package com.github.crybooy007.mq;

import java.util.concurrent.TimeUnit;

import com.github.crybooy007.mq.enums.MqSceneEnum;
import com.github.crybooy007.mq.rabbitmq.producer.MessageProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

import static com.github.crybooy007.mq.enums.MqSceneEnum.TEST_DELAY;

@SpringBootApplication
@RequiredArgsConstructor
public class MqApplication {

	private final RabbitTemplate rabbitTemplate;


	public static void main(String[] args) {
		SpringApplication.run(MqApplication.class, args);
	}

	@Autowired
	@Lazy
	public void contextLoads(MessageProvider messageProvider) throws InterruptedException {

		//发送延时队列
		for (int i = 0; i < 5 ;i++){
			messageProvider.onMessage(TEST_DELAY,"延时3秒",3000);
			System.out.println("发送完毕");
		}


			/*for (int i = 0; i < 5; i++) {

				rabbitTemplate.convertAndSend("delay.exchange", "delay",
						"222",
						message -> {
							message.getMessageProperties().setDelay(3000);
							return message;
						});
				System.out.println("发送完毕");
			}*/
		/*messageProvider.onMessage(MqSceneEnum.TEST_DEAD,"你好吗");
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(20);
				messageProvider.onMessage(MqSceneEnum.TEST_ONE,"我不好");
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();*/
	}

}
