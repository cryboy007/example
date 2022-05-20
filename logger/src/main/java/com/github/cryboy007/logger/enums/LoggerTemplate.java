package com.github.cryboy007.logger.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *@enumName LoggerTemplate
 *@Author HETAO
 *@Date 2022/4/30 16:55
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LoggerTemplate {
	RECORD_OPERATOR("操作人:{} :门店:{}--执行了{}操作"),
	RECORD_OPERATOR2("操作人:${#staff.name} :门店:${#operator}");


	String value;

}
