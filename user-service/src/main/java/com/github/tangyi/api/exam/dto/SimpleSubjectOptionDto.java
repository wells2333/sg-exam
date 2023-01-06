package com.github.tangyi.api.exam.dto;

import lombok.Data;

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
