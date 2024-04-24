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

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subjects")
public class Subjects extends BaseEntity<Subjects> {

	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "subject_id")
	private Long subjectId;

	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "category_id")
	private Long categoryId;

	/**
	 * 题目类型，0：选择题，1：简答题，2：判断题，3：多选题
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Integer type;

	@Column(name = "sort")
	private Integer sort;

	/**
	 * 考试题目对应的分类题目 ID
	 */
	private Long parentId;
}
