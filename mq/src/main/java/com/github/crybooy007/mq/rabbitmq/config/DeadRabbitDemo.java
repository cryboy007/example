package com.github.crybooy007.mq.rabbitmq.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 *@ClassName DeadRabbitDemo
 *@Author tao.he
 *@Since 2022/3/27 10:32
 */
@Configuration
public class DeadRabbitDemo {

		// 创建延时交换机
		@Bean
		public Exchange delayExchange(){
			return ExchangeBuilder.directExchange("delay.exchange").delayed().durable(true).build();
		}

		// 创建队列
		@Bean
		public Queue delayQueue(){
			return QueueBuilder.durable("delay.queue").build();
		}

		// 创建延时交换机和队列的绑定关系
		@Bean
		public Binding delayBinding(@Qualifier("delayExchange") Exchange exchange, @Qualifier("delayQueue") Queue queue){
			return BindingBuilder.bind(queue).to(exchange).with("delay").noargs();
		}

		/*public static final String BUSINESS_EXCHANGE_NAME = "dead.letter.demo.simple.business.exchange";
		public static final String BUSINESS_QUEUEA_NAME = "dead.letter.demo.simple.business.queuea";
		public static final String BUSINESS_QUEUEB_NAME = "dead.letter.demo.simple.business.queueb";
		public static final String DEAD_LETTER_EXCHANGE = "dead.letter.demo.simple.deadletter.exchange";
		public static final String DEAD_LETTER_QUEUEA_ROUTING_KEY = "dead.letter.demo.simple.deadletter.queuea.routingkey";
		public static final String DEAD_LETTER_QUEUEB_ROUTING_KEY = "dead.letter.demo.simple.deadletter.queueb.routingkey";
		public static final String DEAD_LETTER_QUEUEA_NAME = "dead.letter.demo.simple.deadletter.queuea";
		public static final String DEAD_LETTER_QUEUEB_NAME = "dead.letter.demo.simple.deadletter.queueb";

		//声明业务Exchange
		@Bean("businessExchange")
		public FanoutExchange bussinessExchange() {
			return new FanoutExchange(BUSINESS_EXCHANGE_NAME);
		}

		//声明死信Exchange
		@Bean("deadLetterExchange")
		public DirectExchange deadLetterExchange() {
			return new DirectExchange(DEAD_LETTER_EXCHANGE);
		}

		//声明业务队列A
		@Bean("businessQueueA")
		public Queue businessQueueA() {
			Map<String, Object> args = new HashMap<>(2);
			// x-dead-letter-exchange  这里声明当前队列绑定的死信交换机
			args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
			// x-dead-letter-routing-key  这里声明当前队列的死信路由key
			args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEA_ROUTING_KEY);
			return QueueBuilder.durable(BUSINESS_QUEUEA_NAME).withArguments(args).build();
		}

		//声明业务队列A
		@Bean("businessQueueB")
		public Queue businessQueueB() {
			Map<String, Object> args = new HashMap<>(2);
			// x-dead-letter-exchange  这里声明当前队列绑定的死信交换机
			args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
			// x-dead-letter-routing-key  这里声明当前队列的死信路由key
			args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEA_ROUTING_KEY);
			return QueueBuilder.durable(BUSINESS_QUEUEA_NAME).withArguments(args).build();
		}

		// 声明死信队列A
		@Bean("deadLetterQueueA")
		public Queue deadLetterQueueA(){
			return new Queue(DEAD_LETTER_QUEUEA_NAME);
		}

		// 声明死信队列B
		@Bean("deadLetterQueueB")
		public Queue deadLetterQueueB(){
			return new Queue(DEAD_LETTER_QUEUEB_NAME);
		}

		// 声明业务队列A绑定关系
		@Bean
		public Binding businessBindingA(@Qualifier("businessQueueA") Queue queue,
				@Qualifier("businessExchange") FanoutExchange exchange){
			return BindingBuilder.bind(queue).to(exchange);
		}

		// 声明业务队列B绑定关系
		@Bean
		public Binding businessBindingB(@Qualifier("businessQueueB") Queue queue,
				@Qualifier("businessExchange") FanoutExchange exchange){
			return BindingBuilder.bind(queue).to(exchange);
		}

		// 声明死信队列A绑定关系
		@Bean
		public Binding deadLetterBindingA(@Qualifier("deadLetterQueueA") Queue queue,
				@Qualifier("deadLetterExchange") DirectExchange exchange){
			return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEA_ROUTING_KEY);
		}

		// 声明死信队列B绑定关系
		@Bean
		public Binding deadLetterBindingB(@Qualifier("deadLetterQueueB") Queue queue,
				@Qualifier("deadLetterExchange") DirectExchange exchange){
			return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEB_ROUTING_KEY);
		}*/
}
