package com.github.cryboy007.logger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.cryboy007.logger.enums.LoggerTemplate;

/**
 *@InterfaceName LogRecord
 *@Author HETAO
 *@Date 2022/4/30 16:59
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecord {
	String operator() default "";

	String bizNo() default "";

	LoggerTemplate template();

	String[] spelValue() default {};

	String[] function() default {};

}
