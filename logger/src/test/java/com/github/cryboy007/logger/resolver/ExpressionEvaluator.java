package com.github.cryboy007.logger.resolver;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;

/**
 *@ClassName ExpressionEvaluatorTest
 *@Author tao.he
 *@Since 2022/4/30 18:16
 */
public class ExpressionEvaluator<T> extends CachedExpressionEvaluator {

	private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

	private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

	/**
	 * 获取EL表达式解析结果
	 * @param conditionExpression  条件表达式
	 * @param method  表达式作用的方法对象
	 * @param args  方法里的参数
	 * @param targetClass 该方法的class
	 * @param returnClazz 解析结果的class
	 * @return
	 */
	public T condition(String conditionExpression, Method method, Object[] args,Class targetClass, Class<T> returnClazz) {
		AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
		// 第一个参数root这里定义成目标类class对象，使用  '#root.name'= targetClass.getName(); 自己随便定义一个root即可
		MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(targetClass, method, args, paramNameDiscoverer);
		return getExpression(conditionCache, annotatedElementKey, conditionExpression).getValue(context, returnClazz);
	}

	// -----------------下面是测试内容可以删除掉-------------------------

	public static void main(String[] args) throws NoSuchMethodException {
		ExpressionEvaluator<String> evaluator = new ExpressionEvaluator<>();
		Object[] arg = new Object[1];
		arg[0] = new TestUser(120,"吕布");
//		Method method = ExpressionEvaluator.class.getMethod("testExpression", TestUser.class);
//		String condition = evaluator.condition("#user.userName", method, arg, SysUser.class, String.class);
//		String condition2 = evaluator.condition("#root.simpleName", method, arg, SysUser.class, String.class);
//		System.out.println(condition);
//		System.out.println(condition2);

	}

	public void testExpression(TestUser user){

	}

	@Data
	@AllArgsConstructor
	public static class TestUser{
		private Integer id;
		private String userName;
	}

}

