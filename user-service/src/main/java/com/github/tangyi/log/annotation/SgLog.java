package com.github.tangyi.log.annotation;

import com.github.tangyi.log.constants.OperationType;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author tangyi
 * @date 2019/3/12 23:50
 */
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
