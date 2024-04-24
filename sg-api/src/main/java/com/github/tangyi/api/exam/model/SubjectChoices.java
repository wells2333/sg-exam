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

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 选择题
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subject_choices")
public class SubjectChoices extends BaseSubject<SubjectChoices> {

	/**
	 * 题目类型，0：单选，1：多选
	 */
	@NotBlank(message = "题目类型不能为空")
	@Column(name = "choices_type")
	private Integer choicesType;

	/**
	 * 选项列表
	 */
	@Transient
	private List<SubjectOption> options;
}
