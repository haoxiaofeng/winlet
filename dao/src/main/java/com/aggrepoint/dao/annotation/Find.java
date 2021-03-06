package com.aggrepoint.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Finds.class)
public @interface Find {
	String value() default "";

	String when() default "";

	String sql() default "";

	String count() default "";

	Class<?> entity() default Object.class;
}
