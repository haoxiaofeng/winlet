package com.aggrepoint.winlet.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Returns.class)
public @interface Return {
	static final String NOT_SPECIFIED = "!!NOT_SPECIFIED!!";

	String value() default NOT_SPECIFIED;

	String rule() default "";

	String update() default "";

	/** 用于容器窗口中执行的Action，值为window表示把返回的页面内容用于更新Container所属的Winlet，而不只是容器窗口。暂不支持其它值 */
	/** 也可以用于dialog中执行的action，值为window表示把返回页面的内容用于更新弹出dialog的window窗口，而不是更新dialog内容 */
	String target() default "";

	boolean dialog() default false;

	boolean cache() default false;

	String log() default "";

	String msg() default "";

	String logexclude() default "";

	String title() default "";

	String view() default NOT_SPECIFIED;
}
