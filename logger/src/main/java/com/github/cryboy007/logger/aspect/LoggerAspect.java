package com.github.cryboy007.logger.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

import javax.annotation.Resource;

import com.github.cryboy007.logger.annotation.LogRecord;
import com.github.cryboy007.logger.enums.LoggerTemplate;
import com.github.cryboy007.logger.resolver.LogRecordContext;
import com.github.cryboy007.logger.resolver.LogRecordExpressionEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 *@ClassName LoggerAspect
 *@Author tao.he
 *@Since 2022/4/30 16:41
 */
@Order(1)
@Aspect
@Slf4j
@Component
public class LoggerAspect {

	private final ExpressionParser parser = new SpelExpressionParser();

	@Resource
	private LogRecordExpressionEvaluator logRecordExpressionEvaluator;

	/**
	 * Pointcut注解声明切点
	 * 配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
	 * @within 对类起作用，@annotation 对方法起作用
	 */
	@Pointcut("@annotation(com.github.cryboy007.logger.annotation.LogRecord)")
	public void loggerPointcut(){}

	/**
	 *
	 */
	@Around("@annotation(logRecordMethod)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint,LogRecord logRecordMethod) throws Throwable{
		LoggerTemplate loggerTemplate = logRecordMethod.template();
		String template = loggerTemplate.getValue();
		String[] expressions = logRecordMethod.spelValue();
		LogRecordContext.putEmptySpan();
		//初始化spel
		EvaluationContext ctx = initContextVariable(proceedingJoinPoint);

		Class<?> aClass = proceedingJoinPoint.getTarget().getClass();
		AnnotatedElementKey annotatedElementKey = getAnnotatedElementKey(proceedingJoinPoint, aClass);

		Function<String, Object> function = item -> {
			try {
				//return parser.parseExpression(item).getValue(ctx); 替换成缓存获取
				return logRecordExpressionEvaluator.parseExpression(item,annotatedElementKey,ctx);
			}
			catch (Exception ignored) {
			}
			return item;
		};
		Object[] params = Arrays.stream(expressions).map(function).toArray();
		log.info(template,params);
		return proceedingJoinPoint.proceed();

	}

	private AnnotatedElementKey getAnnotatedElementKey(ProceedingJoinPoint proceedingJoinPoint, Class<?> aClass) {
		Signature signature = proceedingJoinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature)signature;
		Method targetMethod = methodSignature.getMethod();

		return new AnnotatedElementKey(targetMethod, aClass);
	}

	/**
	 * 初始化EvaluationContext
	 */
	private EvaluationContext initContextVariable(ProceedingJoinPoint joinPoint) {
		EvaluationContext ctx = new StandardEvaluationContext();
		Object[] paramValues = joinPoint.getArgs();
		String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
		for (int i = 0; i < paramNames.length; i++) {
			ctx.setVariable(paramNames[i], paramValues[i]);
		}
		return ctx;
	}
}
