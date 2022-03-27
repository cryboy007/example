package com.github.crybooy007.mq.rabbitmq.consumer;

import java.io.IOException;

import com.github.crybooy007.mq.enums.InitQueueEnum;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *@ClassName DeadMqConsumer
 *@Author tao.he
 *@Since 2022/3/27 10:17
 */
@Slf4j
@Component
public class DeadMqConsumer {

	/**
	 * 死信队列
	 */
	@RabbitListener(
			bindings = @QueueBinding(
					//队列名称
					value    = @Queue(value = "rabbitmq.dead.one", durable = "true"),
					//交换机 可以理解为模块
					exchange = @Exchange(value = "direct.dead",
							durable = "true",
							type = ExchangeTypes.DIRECT,
							ignoreDeclarationExceptions = "true"
					),key = "business.exception"
			),containerFactory = "customSimpleRabbitFactory"
	)
	public void omMessage(Message message, Channel channel) throws IOException {
		System.out.println("收到死信消息A：" + new String(message.getBody()));
		log.info("死信消息properties:{}", message.getMessageProperties());
		//channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
	}

	/**
	 * 业务对象
	 */
	@RabbitListener(
			bindings = @QueueBinding(
				//队列名称
				value    = @Queue(value = "rabbitmq.number.two", durable = "true",
									//配置死信队列
									arguments = {
											//x-message-ttl：指定消息过期TTL
											//x-max-length :指定队列的最大长度
											@Argument(name = "x-dead-letter-exchange",value = "direct.dead"),
											@Argument(name = "x-dead-letter-routing-key",value = "business.exception"),
											//绑定关系 可以理解为场景关联
									} ),
				//交换机 可以理解为模块
				exchange = @Exchange(value = "fanout.business",
						durable = "true",
						type = ExchangeTypes.FANOUT,
						ignoreDeclarationExceptions = "true"
			),key = "business.two"
		),containerFactory = "customSimpleRabbitFactory"
	)
	public void businessTwo(Message message,Channel channel) throws IOException {
		long tag = message.getMessageProperties().getDeliveryTag();
		try {
			doSomething("业务TWO场景开始消费----------------->>>>>>>>>>");
			channel.basicAck(tag,true);
		}catch (Exception e) {
			log.error("消息消费发生异常, error msg:{}", e.getMessage());
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, false);
		}
	}

	private void doSomething(String x) {
		System.out.println(x);
		throw new RuntimeException("模拟异常");
	}
}
