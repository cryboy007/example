package com.github.cryboy007.logger.resolver;

import java.util.Map;
import java.util.Stack;

/**
 *@ClassName LogRecordContext
 *@Author tao.he
 *@Since 2022/4/30 18:01
 */
public class LogRecordContext {
	private static final InheritableThreadLocal<Stack<Map<String, Object>>> variableMapStack = new InheritableThreadLocal<>();

	static Map<String, Object> getVariables() {
		return variableMapStack.get().peek();
	}

	public synchronized static void putEmptySpan() {
		if (variableMapStack.get() == null) {
			variableMapStack.set(new Stack<>());
		}
	}
}
