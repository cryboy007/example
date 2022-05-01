package com.github.cryboy007.logger.service;

/**
 *@InterfaceName IParseFunction
 *@Author HETAO
 *@Date 2022/5/1 12:24
 */
public interface IParseFunction {
	default boolean executeBefore(){
		return false;
	}

	String functionName();

	String apply(String value);
}
