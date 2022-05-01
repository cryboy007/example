package com.github.cryboy007.logger.service;

import java.util.function.BiFunction;

/**
 *@InterfaceName IFunctionService
 *@Author HETAO
 *@Date 2022/5/1 12:23
 */
public interface IFunctionService extends BiFunction<String,String,String> {

	default boolean beforeFunction(String functionName){
		return false;
	}

}
