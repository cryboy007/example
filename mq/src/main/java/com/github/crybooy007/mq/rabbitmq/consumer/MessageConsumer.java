package com.github.crybooy007.mq.rabbitmq.consumer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *@ClassName MessageConsumer
 *@Author tao.he
 *@Since 2022/3/25 21:12
 */
@Component
@Slf4j
public class MessageConsumer //implements ChannelAwareMessageListener
{
	private final AtomicInteger count = new AtomicInteger(0);

	private Consumer consumer;

/*	@Autowired
	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}


	@RabbitListener(queues = "flush.cache.queue")
	public void flushCache(Message message,Channel channel) throws IOException {
		channel.basicConsume("flush.cache.queue",true,consumer);
	}*/

	@RabbitListener(
			bindings = @QueueBinding(
					//队列名称
					value    = @Queue(value = "rabbitmq.number.one", durable = "true"),
					//交换机 可以理解为模块
					exchange = @Exchange(value = "direct.test",
							durable = "true",
							type = ExchangeTypes.DIRECT,
							ignoreDeclarationExceptions = "true"),
					//绑定关系 可以理解为场景关联
					key = "number.one")
			,containerFactory = "customSimpleRabbitFactory"
	)
	public void onMessage(//String obj,
			Message message, Channel channel) throws IOException {
		//System.out.println("内容;"+obj);
		long tag = message.getMessageProperties().getDeliveryTag();
		try {
			confirmMessage(channel, tag);
			channel.basicAck(tag,true);
		}catch (Exception e) {
			log.info("异常:次数{}",count.addAndGet(1));
			//throw e;
			//requeue 是否重新进入队列
			channel.basicNack(tag,true,false);
		}
	}

	private void confirmMessage(Channel channel, long tag) throws IOException {
		throw new RuntimeException("系统异常");
		/*System.out.println("监听消息-->>>>");
		channel.basicAck(tag,true);*/
	}
}
