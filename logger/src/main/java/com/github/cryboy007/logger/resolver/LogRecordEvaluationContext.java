package com.github.cryboy007.logger.resolver;

import com.github.cryboy007.logger.service.IParseFunction;
import com.github.cryboy007.logger.utils.SpringContextUtil;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *@ClassName LogRecordEvaluationContext
 *@Author tao.he
 *@Since 2022/4/30 18:00
 */
public class LogRecordEvaluationContext extends MethodBasedEvaluationContext {
	public LogRecordEvaluationContext(Object rootObject, Method method, Object[] arguments,
			ParameterNameDiscoverer parameterNameDiscoverer, Object ret, String errorMsg) {
		//把方法的参数都放到 SpEL 解析的 RootObject 中
		super(rootObject, method, arguments, parameterNameDiscoverer);
		//把 LogRecordContext 中的变量都放到 RootObject 中
		Map<String, Object> variables = LogRecordContext.getVariables();
		if (variables != null && variables.size() > 0) {
			for (Map.Entry<String, Object> entry : variables.entrySet()) {
				setVariable(entry.getKey(), entry.getValue());
			}
		}
		final IParseFunction operator = SpringContextUtil.getBeanById("defaultParseFunction", IParseFunction.class);
		final Map<String, IParseFunction> beans = SpringContextUtil.getApplicationContext().getBeansOfType(IParseFunction.class);
		setVariable("operator",operator.apply(null));
		//把方法的返回值和 ErrorMsg 都放到 RootObject 中
		setVariable("_ret", ret);
		setVariable("_errorMsg", errorMsg);
	}
}
