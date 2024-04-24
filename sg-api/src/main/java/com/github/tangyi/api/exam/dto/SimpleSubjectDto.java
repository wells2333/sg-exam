/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

	/**
	 * 材料附属题
	 */
	private List<SimpleSubjectDto> childSubjects;

	/**
	 * 材料附属题数量
	 */
	private Integer childLength;


	private Integer sort;

	private String speechUrl;

	private Integer speechPlayLimit;

	private Integer autoPlaySpeech;

	private String subjectVideoUrl;
}
