package com.github.tangyi.api.exam.dto;

import lombok.Data;

/**
 *
 * @author tangyi
 * @date 2022/4/14 1:26 下午
 */
@Data
public class SimpleSubjectOptionDto {

	/**
	 * 选项名称
	 */
	private String optionName;

	/**
	 * 选项内容
	 */
	private String optionContent;

	private Integer sort;
}
