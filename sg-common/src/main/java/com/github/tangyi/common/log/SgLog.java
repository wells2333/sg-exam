package com.github.tangyi.common.log;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SgLog {

	/**
	 * 描述
	 *
	 * @return {String}
	 */
	String value() default "";

	/**
	 * 操作类型
	 *
	 * @return OperationType
	 */
	OperationType operationType() default OperationType.OTHER;
}
