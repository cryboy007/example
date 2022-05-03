package com.github.cryboy007.logger.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *@ClassName LogRecordContext
 *@Author tao.he
 *@Since 2022/4/30 18:01
 */
public class LogRecordContext {
	private static final InheritableThreadLocal<Stack<Map<String, Object>>> variableMapStack = new InheritableThreadLocal<>();

	public synchronized static Map<String, Object> getVariables() {
		if (variableMapStack.get() == null) {
			putEmptySpan();
			return getVariables();
		}
		return variableMapStack.get().peek();
	}

	public synchronized static void putEmptySpan() {
		if (variableMapStack.get() == null) {
			Stack<Map<String, Object>> stack = new Stack<>();
			stack.push(new HashMap<>());
			variableMapStack.set(stack);
		}else {
			Stack<Map<String, Object>> stack = variableMapStack.get();
			stack.push(new HashMap<>());
			variableMapStack.set(stack);
		}
	}

	public static void putVariable(String k,Object v) {
		Map<String, Object> map = variableMapStack.get().peek();
		map.put(k,v);
	}

	public static void clear() {
		variableMapStack.get().pop();
	}
}
