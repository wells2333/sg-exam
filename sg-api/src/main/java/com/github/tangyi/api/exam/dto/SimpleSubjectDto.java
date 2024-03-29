package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleSubjectDto {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 题目名称
	 */
	private String subjectName;

	private Integer type;

	private String typeLabel;

	/**
	 * 选择题类型
	 */
	private Integer choicesType;

	/**
	 * 分值
	 */
	private Double score;

	/**
	 * 难度等级
	 */
	private Integer level;

	/**
	 * 选项列表
	 */
	private List<SimpleSubjectOptionDto> options;

	private Integer sort;

	private String speechUrl;

	private Integer speechPlayLimit;

	private Integer autoPlaySpeech;

	private String subjectVideoUrl;
}
