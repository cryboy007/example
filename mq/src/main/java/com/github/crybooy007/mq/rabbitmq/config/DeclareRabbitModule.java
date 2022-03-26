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
					DirectExchange directExchange = new DirectExchange(rabbitEnum.getExchange(), true, false);
					HashMap<String, Object> arguments = Maps.newHashMap();
					// x-dead-letter-exchange  这里声明当前队列绑定的死信交换机
					arguments.put("x-dead-letter-exchange", rabbitEnum.getExchange());
					// x-dead-letter-routing-key  这里声明当前队列的死信路由key
					arguments.put("x-dead-letter-routing-key", rabbitEnum.getRouteKey());
					Queue queue = new Queue(rabbitEnum.getQueue(), true,false,false, arguments);
					amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(rabbitEnum.getQueue()));
				}else {
					DirectExchange directExchange = new DirectExchange(rabbitEnum.getExchange(), true, false);
					Queue queue = new Queue(rabbitEnum.getQueue(), true);
					amqpAdmin.declareExchange(directExchange);
					amqpAdmin.declareQueue(queue);
					amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(rabbitEnum.getQueue()));
				}
			}
		}
		log.info("init RabbitEnum complete.");
	}


}