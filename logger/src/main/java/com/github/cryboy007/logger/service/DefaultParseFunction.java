package com.github.cryboy007.logger.service;

import org.springframework.stereotype.Service;

/**
 *@ClassName OperatorFunction
 *@Author tao.he
 *@Since 2022/5/1 12:33
 */
@Service("defaultParseFunction")
public class DefaultParseFunction implements IParseFunction{
	@Override
	public String functionName() {
		return "operator";
	}

	@Override
	public String apply(String value) {
		//模拟查询数据
		return getOperator(value);
	}

	private String getOperator(String value) {
		return "张三";
	}

}
