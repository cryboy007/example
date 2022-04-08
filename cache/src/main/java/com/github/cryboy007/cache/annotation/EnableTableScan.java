package com.github.cryboy007.cache.annotation;

import org.springframework.context.annotation.ComponentScans;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
public @interface EnableTableScan {

    boolean value() default true;
}
