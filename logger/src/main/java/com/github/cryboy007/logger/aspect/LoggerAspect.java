package com.github.cryboy007.logger.aspect;

import com.github.cryboy007.logger.annotation.LogRecord;
import com.github.cryboy007.logger.enums.LoggerTemplate;
import com.github.cryboy007.logger.exception.MethodExecuteResult;
import com.github.cryboy007.logger.resolver.LogRecordContext;
import com.github.cryboy007.logger.resolver.LogRecordEvaluationContext;
import com.github.cryboy007.logger.resolver.LogRecordValueParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
	private LogRecordValueParser.LogRecordExpressionEvaluator logRecordExpressionEvaluator;



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

			if (bizNo.length() > 0) {
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
		LogRecordEvaluationContext defaultExpress =
				new LogRecordEvaluationContext(ret,method,args,discoverer,ret,errorMsg);
		Map<String, Object> map = LogRecordContext.getVariables();
		AnnotatedElementKey annotatedElementKey = getAnnotatedElementKey(targetClass,method);
		if (success) {
			String template = (String) map.get("template");
			final String content = logRecordExpressionEvaluator.parseExpression(template, annotatedElementKey, defaultExpress);
			log.info(content);
		}else {
			log.error(errorMsg);
		}


	}

	private AnnotatedElementKey getAnnotatedElementKey(Class<?> aClass,Method targetMethod) {
		return new AnnotatedElementKey(targetMethod, aClass);
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
