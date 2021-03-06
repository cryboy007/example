package com.github.crybooy007.mq.rabbitmq.producer;

import com.alibaba.fastjson.JSON;
import com.github.crybooy007.mq.enums.MqSceneEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 *@ClassName MessageConsumer
 *@Author tao.he
 *@Since 2022/3/25 20:52
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProvider {

	private final RabbitTemplate rabbitTemplate;



	public void onMessage(MqSceneEnum mqSceneEnum,Object obj) {
		String msgStr = JSON.toJSONString(obj);
		rabbitTemplate.setExchange(mqSceneEnum.getExchange());
		rabbitTemplate.setRoutingKey(mqSceneEnum.getRoutingKey());
		Message message = MessageBuilder.withBody(msgStr.getBytes(StandardCharsets.UTF_8))
				.setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
		//rabbitTemplate.convertAndSend(message);
		rabbitTemplate.send(message);
	}

	public void onMessage(MqSceneEnum mqSceneEnum,Object obj,Integer delayTime) {
	/*	String msgStr = JSON.toJSONString(obj);
		rabbitTemplate.setExchange(mqSceneEnum.getExchange());
		rabbitTemplate.setRoutingKey(mqSceneEnum.getRoutingKey());
		Message message = MessageBuilder.withBody(msgStr.getBytes(StandardCharsets.UTF_8))
				.setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
		//rabbitTemplate.convertAndSend(message);
		rabbitTemplate.send(message);*/
		String msgStr = JSON.toJSONString(obj);
		rabbitTemplate.convertAndSend(mqSceneEnum.getExchange(), mqSceneEnum.getRoutingKey(), msgStr, a ->{
			a.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
			a.getMessageProperties().setDelay(delayTime);
			return a;
		});
		/*String msgStr = JSON.toJSONString(obj);
		rabbitTemplate.setExchange(mqSceneEnum.getExchange());
		rabbitTemplate.setRoutingKey(mqSceneEnum.getRoutingKey());
		Message message = MessageBuilder.withBody(msgStr.getBytes(StandardCharsets.UTF_8))
				.setContentType(MessageProperties.CONTENT_TYPE_JSON)
				.setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
		//rabbitTemplate.convertAndSend(message);
		rabbitTemplate.convertAndSend(message,a ->{
			a.getMessageProperties().setDelay(delayTime);
			return a;
		});*/
	}
}
