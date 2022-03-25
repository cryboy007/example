package com.github.crybooy007.mq.rabbitmq.consumer;

import java.io.IOException;

import com.github.cryboy007.cmmon.utils.SpringContextUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *@ClassName FlushCacheConsumer
 *@Author tao.he
 *@Since 2022/3/26 0:49
 */
public class FlushCacheConsumer extends DefaultConsumer {
	private static final Logger logger = LoggerFactory.getLogger(FlushCacheConsumer.class);
	/**
	 * Constructs a new instance and records its association to the passed-in channel.
	 * @param channel the channel to which this consumer is attached
	 */
	public FlushCacheConsumer(Channel channel) {
		super(channel);
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
		super.handleConsumeOk(consumerTag);
	}

	@Override
	public void handleCancelOk(String consumerTag) {
		super.handleCancelOk(consumerTag);
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
		super.handleCancel(consumerTag);
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
		super.handleShutdownSignal(consumerTag, sig);
	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		super.handleRecoverOk(consumerTag);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		try {
			long t0 = System.currentTimeMillis();
			String message = new String(body, "utf-8");
			logger.info("rabbitMQ开始更新缓存:{}", message);
			if (SpringContextUtil.getApplicationContext().containsBean(message)) {
				//todo
				logger.info("刷新缓存---->>>>");
				//LocalCache localCache = SpringContextUtil.getBeanById(message);
				//localCache.refresh();
			} else {
				logger.warn("{} bean  not found！", message);
				// do nothing
			}
			logger.info("rabbitMQ更新缓存结束,costs:{}", (System.currentTimeMillis() - t0));
		} catch (Exception e) {
			logger.warn("abbitMQ更新缓存失败,请关注... ");
		}
	}

	@Override
	public Channel getChannel() {
		return super.getChannel();
	}

	@Override
	public String getConsumerTag() {
		return super.getConsumerTag();
	}
}
