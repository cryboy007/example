package com.github.crybooy007.mq.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 *@ClassName SendMqEnum
 *@Author tao.he
 *@Since 2022/3/25 21:02
 */
@Getter
@AllArgsConstructor
public enum MqSceneEnum {
		TEST_ONE("rabbitMq-测试","direct.test","number.one"),
		TEST_DEAD("死信队列测试","fanout.business","business.two"),
		TEST_DELAY("延时队列测试","direct.delayed","delayed.one");
	;
	//场景
	private final String scene;
	//发送mq的 exchange
	private final String exchange;
	//f发送mq 的 routingKey
	private final String routingKey;
}
