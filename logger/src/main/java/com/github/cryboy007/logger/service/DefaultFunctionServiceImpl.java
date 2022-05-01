package com.github.cryboy007.logger.service;

import com.github.cryboy007.logger.factory.ParseFunctionFactory;

/**
 *@ClassName DefaultFunctionServiceImpl
 *@Author tao.he
 *@Since 2022/5/1 12:25
 */
public class DefaultFunctionServiceImpl implements IFunctionService{
	private final ParseFunctionFactory parseFunctionFactory;

	public DefaultFunctionServiceImpl(ParseFunctionFactory parseFunctionFactory) {
		this.parseFunctionFactory = parseFunctionFactory;
	}

	@Override
	public String apply(String functionName, String value) {
		IParseFunction function = parseFunctionFactory.getFunction(functionName);
		if (function == null) {
			return value;
		}
		return function.apply(value);
	}

	@Override
	public boolean beforeFunction(String functionName) {
		return parseFunctionFactory.isBeforeFunction(functionName);
	}
}
