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

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 考试记录 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long examRecordId;

	/**
	 * 题目 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long subjectId;

	/**
	 * 题目类型
	 */
	private Integer type;

	/**
	 * 答案
	 */
	private String answer;

	/**
	 * 答题类型，0：正确，1：错误
	 */
	private Integer answerType;

	/**
	 * 得分
	 */
	private Double score;

	/**
	 * 用户 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 题目详情
	 */
	private SubjectDto subject;

	/**
	 * 批改状态
	 */
	private Integer markStatus;

	/**
	 * 批改人
	 */
	private String markOperator;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 耗时
	 */
	private String duration;

	/**
	 * 语音播放次数
	 */
	private Long speechPlayCnt;
}
