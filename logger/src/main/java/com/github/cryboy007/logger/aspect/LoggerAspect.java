package com.github.cryboy007.logger.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;

import com.github.cryboy007.logger.annotation.LogRecord;
import com.github.cryboy007.logger.enums.LoggerTemplate;
import com.github.cryboy007.logger.exception.MethodExecuteResult;
import com.github.cryboy007.logger.factory.ParseFunctionFactory;
import com.github.cryboy007.logger.resolver.LogRecordContext;
import com.github.cryboy007.logger.resolver.LogRecordEvaluationContext;
import com.github.cryboy007.logger.resolver.LogRecordValueParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
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

	@Resource
	private LogRecordValueParser logRecordValueParser;

	@Resource
	private ParseFunctionFactory parseFunctionFactory;

/*	@Resource
	private LogRecordInterceptor logRecordInterceptor;*/

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
		Object proceed = null;
		LogRecordContext.putEmptySpan();
		MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");

		LoggerTemplate loggerTemplate = logRecordMethod.template();
		String template = loggerTemplate.getValue();
		String bizNo = logRecordMethod.bizNo();
		String[] expressions = logRecordMethod.spelValue();
		LogRecordContext.putVariable("template",template);
		LogRecordContext.putVariable("expressions",expressions);
		Object[] args = proceedingJoinPoint.getArgs();
		Method method = getMethod(proceedingJoinPoint);
		Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
		//业务逻辑执行前的自定义函数解析
		Map<String, String> functionNameAndReturnMap = new HashMap<>();
		try {
			//业务逻辑执行前的自定义函数解析
			//IParseFunction operator = parseFunctionFactory.getFunction("operator");
			//operator.apply("123");
			functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(template, targetClass, method, args);
		}catch (Exception e) {
			log.error("log record parse before function exception", e);
		}
		try {
			proceed  = proceedingJoinPoint.proceed();
		}catch (Exception e){
			methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
		}

		//保存日志
		try {
			EvaluationContext ctx = null;

			if (bizNo != null && bizNo.length() > 0) {
				recordExecute(proceed, method, args, bizNo, targetClass,
						methodExecuteResult.isSuccess(), methodExecuteResult.getErrorMsg(), functionNameAndReturnMap);
			}else {

			}
		}catch (Exception t){
			//记录日志错误不要影响业务
			log.error("log record parse exception", t);
		}finally {
			LogRecordContext.clear();
		}

		//业务异常
		if (methodExecuteResult.getThrowable() != null) {
			throw methodExecuteResult.getThrowable();
		}
		return proceed;

	}

	private Map<String, String> processBeforeExecuteFunctionTemplate(String template, Class<?> targetClass, Method method, Object[] args) {
		return null;
	}

	private void recordExecute(Object ret, Method method, Object[] args, String bizNo, Class<?> targetClass,
			boolean success, String errorMsg, Map<String, String> functionNameAndReturnMap) {
		ParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();
		LogRecordEvaluationContext logRecordEvaluationContext =
				new LogRecordEvaluationContext(ret,method,args,discoverer,ret,errorMsg);
		LogRecordValueParser.LogRecordExpressionEvaluator logRecordExpressionEvaluator = new LogRecordValueParser.LogRecordExpressionEvaluator();
		String[] parameterNames = discoverer.getParameterNames(method);
		EvaluationContext ctx = logRecordValueParser.createEvaluationContext(args, parameterNames,logRecordEvaluationContext);
		//EvaluationContext ctx = initContextVariable(args, parameterNames);
		Map<String, Object> map = LogRecordContext.getVariables();
		AnnotatedElementKey annotatedElementKey = getAnnotatedElementKey(targetClass,method);
		Function<String, Object> function = item -> {
			try {
				//return parser.parseExpression(item).getValue(ctx); 替换成缓存获取
				return logRecordExpressionEvaluator.parseExpression(item,annotatedElementKey, ctx);
			}
			catch (Exception ignored) {
			}
			return item;
		};
		String[] expressions = (String[])map.get("expressions");
		String template = (String) map.get("template");
		Object[] params = Arrays.stream(expressions).map(function).toArray();
		log.info(template,params);


	}

	private AnnotatedElementKey getAnnotatedElementKey(Class<?> aClass,Method targetMethod) {
		AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(targetMethod, aClass);
		/*
		if (logRecordValueParser.LogRecordExpressionEvaluator.getTargetMethodCache().containsKey(annotatedElementKey)) {
			return annotatedElementKey;
		}else {
			logRecordExpressionEvaluator.getTargetMethodCache().put(annotatedElementKey,targetMethod);
		}*/
		return annotatedElementKey;
	}

	private Method getMethod(ProceedingJoinPoint proceedingJoinPoint) {
		Signature signature = proceedingJoinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature)signature;
		return methodSignature.getMethod();
	}

	/**
	 * 初始化EvaluationContext
	 */
	private EvaluationContext initContextVariable(Object[] paramValues,String[] paramNames) {
		EvaluationContext ctx = new StandardEvaluationContext();
		if (paramNames==null) {
			return ctx;
		}
		for (int i = 0; i < paramNames.length; i++) {
			ctx.setVariable(paramNames[i], paramValues[i]);
		}
		return ctx;
	}
}
