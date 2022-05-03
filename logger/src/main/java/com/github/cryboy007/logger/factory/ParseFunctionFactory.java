package com.github.cryboy007.logger.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.cryboy007.logger.service.IParseFunction;
import org.apache.commons.lang3.StringUtils;

import org.springframework.util.CollectionUtils;

/**
 *@ClassName ParseFunctionFactory
 *@Author tao.he
 *@Since 2022/5/1 12:24
 */
public class ParseFunctionFactory {
	private Map<String, IParseFunction> allFunctionMap;

	public ParseFunctionFactory(List<IParseFunction> parseFunctions) {
		if (CollectionUtils.isEmpty(parseFunctions)) {
			return;
		}
		allFunctionMap = new HashMap<>();
		for (IParseFunction parseFunction : parseFunctions) {
			if (StringUtils.isEmpty(parseFunction.functionName())) {
				continue;
			}
			allFunctionMap.put(parseFunction.functionName(), parseFunction);
		}
	}

	public IParseFunction getFunction(String functionName) {
		return allFunctionMap.get(functionName);
	}

	public boolean isBeforeFunction(String functionName) {
		return allFunctionMap.get(functionName) != null && allFunctionMap.get(functionName).executeBefore();
	}
}
