package com.github.cryboy007.logger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *@InterfaceName LogRecordAnnotation
 *@Author HETAO
 *@Date 2022/4/30 16:40
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecordAnnotation {
	String success();

	String fail() default "";

	String operator() default "";

	String bizNo();

	String category() default "";

	String detail() default "";

	String condition() default "";
}
