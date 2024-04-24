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

/**
 * 考试成员 exam_examination_member
 */
@Data
@Table(name = "exam_examination_member")
@EqualsAndHashCode(callSuper = true)
public class ExamPermission extends BaseEntity<ExamPermission> {

	/**
	 * 类型，0：课程，1：考试
	 */
	@Column(name = "exam_type")
	private Integer examType;

	@Column(name = "exam_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long examId;

	/**
	 * 成员类型，0：全部用户，1：用户 ID，2：部门
	 */
	@Column(name = "member_type")
	private Integer memberType;

	/**
	 * 成员 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "member_id")
	private Long memberId;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
