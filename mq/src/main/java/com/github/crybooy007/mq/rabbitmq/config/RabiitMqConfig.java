package com.github.crybooy007.mq.rabbitmq.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.crybooy007.mq.rabbitmq.consumer.FlushCacheConsumer;
import com.github.cryboy007.cmmon.utils.SpringContextUtil;
import com.google.common.collect.Maps;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.sun.istack.internal.NotNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNullApi;

import org.apache.commons.lang3.StringUtils;

/**
 *@ClassName RabiitMqConfig
 *@Author tao.he
 *@Since 2022/3/25 21:58
 */
@Configuration
@ConditionalOnExpression("${spring.rabbitmq.enabled:true}")
public class RabiitMqConfig {


	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.rabbitmq.addresses}")
	private String addresses;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String password;

	@Value("${spring.rabbitmq.notifyExchange}")
	private String notifyExchange;

	public String getNotifyExchange() {
		return notifyExchange;
	}

	public void setNotifyExchange(String notifyExchange) {
		this.notifyExchange = notifyExchange;
	}

	@Bean
	@ConditionalOnMissingBean
	public DeclareRabbitModule declareRabbitModule(AmqpAdmin rabbitModule) {
		return new DeclareRabbitModule(rabbitModule);
	}

	@Bean
	public com.rabbitmq.client.ConnectionFactory initConnectionFactory() {
		com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setAutomaticRecoveryEnabled(true);
		factory.setVirtualHost("/");
		factory.setNetworkRecoveryInterval(10000);
		return factory;
	}

	/**
	 * 因为这个listener container 是自己创建的，会抑制spring boot自动配置
	 ，所以这些配置就不生效了。spring.rabbitmq.listener.*
	 *
	 * @param connectionFactory
	 * @return
	 */
	@Bean("customSimpleRabbitFactory")
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
			ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(jsonConverter());
		//factory.setErrorHandler(errorHandler());
		factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		factory.setConcurrentConsumers(4);  //设置线程数
		factory.setMaxConcurrentConsumers(4); //最大线程数
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	private MessageConverter jsonConverter() {
		//在容器中导入Json的消息转换器
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		// 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
		rabbitTemplate.setMandatory(true);
		rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				System.out.println("ConfirmCallback:     " + "相关数据：" + correlationData);
				System.out.println("ConfirmCallback:     " + "确认情况：" + ack);
				System.out.println("ConfirmCallback:     " + "原因：" + cause);

			}
		});

		rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {

			@Override
			public void returnedMessage(ReturnedMessage returned) {
				System.out.println("ReturnCallback:     " + "消息：" + returned.getMessage());
				System.out.println("ReturnCallback:     " + "回应码：" + returned.getReplyCode());
				System.out.println("ReturnCallback:     " + "回应信息：" + returned.getReplyText());
				System.out.println("ReturnCallback:     " + "交换机：" + returned.getExchange());
				System.out.println("ReturnCallback:     " + "路由键：" + returned.getRoutingKey());
			}
		});
		return rabbitTemplate;
	}

	@Bean
	public Consumer messageContainer(com.rabbitmq.client.ConnectionFactory factory) {
		Connection connection = null;
		try {
			String[] addressesArray = addresses.split(",");
			List<Address> addressList = new ArrayList<>();
			for (String addres : addressesArray) {
				String host = StringUtils.substringBefore(addres, ":");
				String s = StringUtils.substringAfter(addres, ":");
				int port = StringUtils.isEmpty(s) ? 5672 : Integer.valueOf(s);
				addressList.add(new Address(host, port));
			}
			connection = factory.newConnection(addressList.toArray(new Address[0]));
			connection.addShutdownListener(new ShutdownListener() {
				@Override
				public void shutdownCompleted(ShutdownSignalException cause) {
					String hardError = "";
					String applInit = "";
					if (cause.isHardError()) {
						hardError = "connection";
					} else {
						hardError = "channel";
					}
					if (cause.isInitiatedByApplication()) {
						applInit = "application";
					} else {
						applInit = "broker";
					}
					logger.error("Connectivity to MQ has failed.  It was caused by " + applInit + " at the " + hardError
							+ " level.  Reason received " + cause.getReason()
							+ " ,waiting for recovering connection ...");
				}
			});
			//添加恢复监听器
			((Recoverable) connection).addRecoveryListener(new RecoveryListener() {
				@Override
				public void handleRecoveryStarted(Recoverable recoverable) {
					// TODO Auto-generated method stub
				}

				@Override
				public void handleRecovery(Recoverable recoverable) {
					if (recoverable instanceof Connection) {
						logger.info("Connection was recovered.");
					} else if (recoverable instanceof Channel) {
						int channelNumber = ((Channel) recoverable).getChannelNumber();
						logger.info("Connection to channel #" + channelNumber + " was recovered.");
					}
				}
			});
			try (Channel channel = connection.createChannel()) {

				channel.exchangeDeclare(notifyExchange, "fanout");//// 声明一个交换机
				// 临时队列
				//String queueName = channel.queueDeclare().getQueue();
				// 将队列绑定到交换机上
				String queueName = channel.queueDeclare("flush.cache.queue", true, false, false, Maps.newHashMap())
						.getQueue();
				channel.queueBind(queueName, notifyExchange, "");
				logger.info(" [*] Waiting for messages. To exit press CTRL+C");
				FlushCacheConsumer consumer = new FlushCacheConsumer(channel);
				channel.basicConsume(queueName, true, consumer);
				return consumer;
			}
		} catch (Exception e) {
			logger.error("创建rabbitMQ消费者时候出现异常了", e);
		}

		return null;

	}

}
