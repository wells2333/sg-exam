package com.github.tangyi.common.excel.annotation;

import java.lang.annotation.*;

/**
 * @author tangyi
 * @date 2019/12/10 16:04
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelModel {

	/**
	 * 模块名，用于导出时的文件名
	 * @return String
	 */
	String value() default "";

	/**
	 * 页名
	 * @return String
	 */
	String[] sheets() default {"sheet1"};

}
