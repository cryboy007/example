package com.github.crybooy007.mq.rabbitmq.consumer;

import java.io.IOException;
import java.util.Date;

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
 *@ClassName DelayedConsumer
 *@Author tao.he
 *@Since 2022/3/27 12:16
 */
@Component
@Slf4j
public class DelayedConsumer {

	@RabbitListener(
			bindings = @QueueBinding(
					//队列名称
					value    = @Queue(value = "rabbitmq.number.three", durable = "true",
							//配置死信队列
							arguments = {
									//x-message-ttl：指定消息过期TTL
									//x-max-length :指定队列的最大长度
									@Argument(name = "x-dead-letter-exchange",value = "direct.dead"),
									@Argument(name = "x-dead-letter-routing-key",value = "business.exception"),
									//绑定关系 可以理解为场景关联
							} ),
					//交换机 可以理解为模块
					exchange = @Exchange(value = "direct.delayed",
							durable = "true",
							type = "x-delayed-message",
							ignoreDeclarationExceptions = "true",
							arguments = {
								//延时队列
								@Argument(name = "x-delayed-type",value = "direct")
							}
					),key = "delayed.one"
			),containerFactory = "customSimpleRabbitFactory"
	)
	public void onMessage(String data,Message msg, Channel channel) throws IOException {
		long tag = msg.getMessageProperties().getDeliveryTag();
		log.info("当前时间：{},收到请求，msg:{}", new Date(), data);
		channel.basicAck(tag,true);
	}

	@RabbitListener(queues = {"delay.queue"})
	public void delayedMessage(String data,Message msg, Channel channel) throws IOException {
		long tag = msg.getMessageProperties().getDeliveryTag();
		log.info("当前时间：{},收到请求，msg:{}", new Date(), data);
		channel.basicAck(tag,true);
	}

}
