package com.github.cryboy007.resolver;

import com.github.cryboy007.annotation.CustomerParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @ClassName MyArgumentResolver
 * @Author tao.he
 * @Since 2022/3/30 16:45
 */
@Slf4j
public class MyArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CustomerParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 获取http request
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        log.info("当前请求的路径:[{}]", request.getRequestURI());
        // 获取到这个注解
        CustomerParam customerParam = parameter.getParameterAnnotation(CustomerParam.class);
        // 获取在redis中的key
        String redisKey = customerParam.key();
        // 模拟从redis中获取值
        String redisValue = "从redis中获取的值:" + new Random().nextInt(100);
        log.info("从redis中获取到的值为:[{}]", redisValue);
        // 返回值
        return redisValue;
    }
}
