package com.github.tangyi.common.log;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SgLog {

	/**
	 * 描述
	 */
	String value() default "";

	/**
	 * 操作类型
	 */
	OperationType operationType() default OperationType.OTHER;
}
