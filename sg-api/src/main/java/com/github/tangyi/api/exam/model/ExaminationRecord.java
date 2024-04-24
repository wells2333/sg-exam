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

package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_examination_record")
public class ExaminationRecord extends BaseEntity<ExaminationRecord> {

	@NotBlank(message = "用户 ID 不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "user_id")
	private Long userId;

	@NotBlank(message = "考试类型不能为空")
	@Column(name = "type")
	private Integer type;

	@NotBlank(message = "考试 ID 不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "examination_id")
	private Long examinationId;

	/**
	 * 开始时间
	 */
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@Column(name = "end_time")
	private Date endTime;

	/**
	 * 成绩
	 */
	@Column(name = "score")
	private Double score;

	/**
	 * 错误题目数量
	 */
	@Column(name = "in_correct_number")
	private Integer inCorrectNumber;

	/**
	 * 正确题目数量
	 */
	@Column(name = "correct_number")
	private Integer correctNumber;

	/**
	 * 提交状态 1-已提交 0-未提交
	 */
	@NotBlank(message = "状态不能为空")
	@Column(name = "submit_status")
	private Integer submitStatus;
}
