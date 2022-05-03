package com.github.cryboy007.logger.service;

/**
 *@ClassName OperatorFunction
 *@Author tao.he
 *@Since 2022/5/1 12:33
 */
public class DefaultParseFunction implements IParseFunction{
	@Override
	public String functionName() {
		return "default";
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
