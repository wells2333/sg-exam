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
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 考试评价 exam_exam_evaluate
 */
@Data
@Table(name = "exam_exam_evaluate")
@EqualsAndHashCode(callSuper = true)
public class ExamEvaluate extends BaseEntity<ExamEvaluate> {

	/**
	 * 评价内容
	 */
	@Column(name = "evaluate_content")
	private String evaluateContent;

	/**
	 * 用户 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "evaluate_level")
	private Integer evaluateLevel;

	/**
	 * 考试 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "exam_id")
	private Long examId;

	@Column(name = "operator_name")
	private String operatorName;

	/**
	 * 考试名称
	 */
	@Transient
	private String examinationName;

	@Transient
	private String evaluateTime;

	@Transient
	private String avatarUrl;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
