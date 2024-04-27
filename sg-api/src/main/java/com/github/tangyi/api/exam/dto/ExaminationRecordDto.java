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
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExaminationRecordDto extends BaseEntity<ExaminationRecordDto> {

	/**
	 * 考生 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;

	/**
	 * 考试 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long examinationId;

	/**
	 * 考试名称
	 */
	private String examinationName;

	/**
	 * 考试类型
	 */
	private Integer type;

	/**
	 * 考试类型名称
	 */
	private String typeLabel;

	/**
	 * 考试注意事项
	 */
	private String attention;

	/**
	 * 当前时间
	 */
	private Date currentTime;

	/**
	 * 考试开始时间
	 */
	private Date startTime;

	/**
	 * 考试结束时间
	 */
	private Date endTime;

	/**
	 * 考试持续时间
	 */
	private String duration;

	/**
	 * 总分
	 */
	private Integer totalScore;

	/**
	 * 分数
	 */
	private Double score;

	/**
	 * 考试状态
	 */
	private Integer status;

	/**
	 * 封面
	 */
	private String avatar;

	/**
	 * 学院
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long collegeId;

	/**
	 * 专业
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long majorId;

	/**
	 * 课程
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long courseId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 考生名称
	 */
	private String userName;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 错误题目数量
	 */
	private Integer inCorrectNumber;

	/**
	 * 正确题目数量
	 */
	private Integer correctNumber;

	/**
	 * 提交状态 1-已提交 0-未提交
	 */
	private Integer submitStatus;

	private String submitStatusName;

	/**
	 * 答题列表
	 */
	private List<AnswerDto> answers;

}
