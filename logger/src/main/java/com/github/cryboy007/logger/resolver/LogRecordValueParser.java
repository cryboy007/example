package com.github.cryboy007.logger.resolver;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *@ClassName LogRecordExpressionEvaluator
 *@Author tao.he
 *@Since 2022/4/30 17:54
 */
@Component
public class LogRecordValueParser extends CachedExpressionEvaluator {
	/*public EvaluationContext createEvaluationContext(
		Method method, Object[] args, Object target, Class<?> targetClass, Method targetMethod,
		@Nullable Object result, @Nullable BeanFactory beanFactory) {
	EvaluationContext evaluationContext = new StandardEvaluationContext();

	return evaluationContext;
}*/
	public  EvaluationContext createEvaluationContext(Object[] paramValues, String[] paramNames,LogRecordEvaluationContext logRecordEvaluationContext) {
		EvaluationContext ctx = new StandardEvaluationContext();
		if (paramNames == null) {
			return ctx;
		}
		for (int i = 0; i < paramNames.length; i++) {
			ctx.setVariable(paramNames[i], paramValues[i]);
		}
		return ctx;
	}

	public static class LogRecordExpressionEvaluator extends CachedExpressionEvaluator {


		private Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

		private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

		public String parseExpression(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
			//final Expression expression = getExpression(this.expressionCache, methodKey, conditionExpression);
			//支持模板解析
			final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
			final Expression parseExpression = spelExpressionParser.parseExpression(conditionExpression, TEMPLATE_EXPRESSION);
			return parseExpression.getValue(evalContext, String.class);
		}

		public Map<AnnotatedElementKey, Method> getTargetMethodCache() {
			return targetMethodCache;
		}



		ParserContext TEMPLATE_EXPRESSION = new ParserContext() {

			@Override
			public boolean isTemplate() {
				return true;
			}

			@Override
			public String getExpressionPrefix() {
				return "${";
			}

			@Override
			public String getExpressionSuffix() {
				return "}";
			}
		};
	}
}
