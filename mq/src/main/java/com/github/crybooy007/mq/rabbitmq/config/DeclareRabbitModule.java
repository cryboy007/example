package com.github.crybooy007.mq.rabbitmq.config;

import com.github.crybooy007.mq.enums.InitQueueEnum;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.HashMap;

@Slf4j
public class DeclareRabbitModule implements SmartInitializingSingleton {

    private AmqpAdmin amqpAdmin;


    public DeclareRabbitModule(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @Override
    public void afterSingletonsInstantiated() {
        log.info("动态创建MQ配置信息...");
        try {
        	initMQ();
		} catch (Exception e) {
			// 当MQ服务不存在时这边会抛出异常，所以增加try-catch
			log.error(e.getMessage());
		}
    }

	/**
	 * 初始化mq信息
	 */
	private void initMQ() {
		log.info("init RabbitEnum config...");
		InitQueueEnum[] values = InitQueueEnum.values();
		if (values != null && values.length > 0) {
			for (InitQueueEnum rabbitEnum : values) {
				//死信队列
				if (rabbitEnum.getExchange().startsWith("DEAL")) {
					/*DirectExchange directExchange = new DirectExchange(rabbitEnum.getExchange(), true, false);
					HashMap<String, Object> arguments = Maps.newHashMap();
					// x-dead-letter-exchange  这里声明当前队列绑定的死信交换机
					arguments.put("x-dead-letter-exchange", rabbitEnum.getExchange());
					// x-dead-letter-routing-key  这里声明当前队列的死信路由key
					arguments.put("x-dead-letter-routing-key", rabbitEnum.getRouteKey());
					Queue queue = new Queue(rabbitEnum.getQueue(), true,false,false, arguments);
					amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(rabbitEnum.getQueue()));*/
					/**
					 * 注意：在同时启用消息回退机制和备份交换机的情况下，备份交换机的优先级高于消息回退机制。
					 * 也就是说，无法路由到队列的消息将直接交给备份交换机，而不会回退给生产者的回调方法
					 *
					 * ExchangeBuilder.directExchange("confirm.exchange").alternate() alternate指定备份交换机
					 */
				}else {
					DirectExchange directExchange = new DirectExchange(rabbitEnum.getExchange(), true, false);
					Queue queue = new Queue(rabbitEnum.getQueue(), true);
					//使用Builder构造会更优雅
					//QueueBuilder.durable(rabbitEnum.getQueue()).withArguments().build();
					amqpAdmin.declareExchange(directExchange);
					amqpAdmin.declareQueue(queue);
					amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(rabbitEnum.getQueue()));
				}
			}
		}
		log.info("init RabbitEnum complete.");
	}


}