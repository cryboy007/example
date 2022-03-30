package com.github.cryboy007.interceptior;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;

/**
 * @ClassName CustomDeferredResultProcessingInterceptor
 * @Author tao.he
 * @Since 2022/3/30 11:19
 */
@Slf4j
public class CustomDeferredResultProcessingInterceptor implements DeferredResultProcessingInterceptor{
    @Override
    public <T> void beforeConcurrentHandling(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        log.info("beforeConcurrentHandling");
    }

    @Override
    public <T> void preProcess(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        log.info("preProcess");
    }

    @Override
    public <T> void postProcess(NativeWebRequest request, DeferredResult<T> deferredResult, Object concurrentResult) throws Exception {
        log.info("postProcess");
    }

    /**
     * 返回 false ,则不会在此进入下个拦截器进行处理
     */
    @Override
    public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        log.info("handleTimeout");
        return true;
    }

    /**
     * 返回 false ,则不会在此进入下个拦截器进行处理
     */
    @Override
    public <T> boolean handleError(NativeWebRequest request, DeferredResult<T> deferredResult, Throwable t) throws Exception {
        log.info("handleError");
        return true;
    }

    @Override
    public <T> void afterCompletion(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        log.info("-->afterCompletion");
    }
}
