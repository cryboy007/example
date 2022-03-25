package com.github.crybooy007.mq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *@ClassName InitQueueEnum
 *@Author tao.he
 *@Since 2022/3/25 22:09
 */
@Getter
@AllArgsConstructor
public enum InitQueueEnum {
		AUTO_QUEUE_ONE("AUTO_INIT_EXCHANGE_ONE","AUTO_INIT_QUEUE");
	;
	private final String exchange;

	private final String queue;
}
