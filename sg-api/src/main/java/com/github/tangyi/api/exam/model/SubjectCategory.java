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

/**
 * 题目分类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subject_category")
public class SubjectCategory extends BaseEntity<SubjectCategory> {

	/**
	 * 分类名称
	 */
	@NotBlank(message = "分类名称不能为空")
	@Column(name = "category_name")
	private String categoryName;

	/**
	 * 分类描述
	 */
	@Column(name = "category_desc")
	private String categoryDesc;

	/**
	 * 父分类 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "parent_id")
	private Long parentId;

	/**
	 * 排序号
	 */
	@Column(name = "sort")
	private Integer sort;

	/**
	 * 类型：0-私共，1-公有
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 状态：0：草稿，1：已发布
	 */
	@Column(name = "status")
	private Integer status;
}
