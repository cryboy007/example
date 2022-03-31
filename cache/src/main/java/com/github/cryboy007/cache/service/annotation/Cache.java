package com.github.cryboy007.cache.service.annotation;

import com.github.cryboy007.cache.service.common.impl.BaseCacheServiceImpl;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    /**
     * bo对应的缓存类
     *
     * @return
     */
    Class<? extends BaseCacheServiceImpl> value();

}