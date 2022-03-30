package com.github.cryboy007.config;

import com.github.cryboy007.resolver.MyArgumentResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName CustomHandlerMethodArgumentResolverConfig
 * @Author tao.he
 * @Since 2022/3/30 17:31
 */
@Component
public class CustomHandlerMethodArgumentResolverConfig implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RequestMappingHandlerAdapter) {
            final RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
            final List<HandlerMethodArgumentResolver> argumentResolvers = Optional.ofNullable(adapter.getArgumentResolvers())
                    .orElseGet(ArrayList::new);
            final ArrayList<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>(argumentResolvers);
            // 将我们自己的参数解析器放置到第一位
            handlerMethodArgumentResolvers.add(0, new MyArgumentResolver());
            adapter.setArgumentResolvers(Collections.unmodifiableList(handlerMethodArgumentResolvers));
            return adapter;
        }
        return bean;
    }
}
