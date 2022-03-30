package com.github.cryboy007.config;

import com.github.cryboy007.interceptior.CustomAsyncHandlerInterceptor;
import com.github.cryboy007.interceptior.CustomDeferredResultProcessingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebConfi
 * @Author tao.he
 * @Since 2022/3/30 11:18
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 默认超时时间 60s
        configurer.setDefaultTimeout(60000);
        // 注册 deferred result 拦截器
        configurer.registerDeferredResultInterceptors(new CustomDeferredResultProcessingInterceptor());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomAsyncHandlerInterceptor()).addPathPatterns("/**");
    }
}
