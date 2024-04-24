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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.api.exam.model.SubjectCategory;
import com.github.tangyi.common.base.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubjectCategoryDto extends TreeEntity<SubjectCategoryDto> {

	/**
	 * 分类名称
	 */
	private String categoryName;

	/**
	 * 分类描述
	 */
	private String categoryDesc;

	/**
	 * 父分类 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

	private Integer type;

	private Integer subjectCnt;

	private Integer status;

	public SubjectCategoryDto(SubjectCategory subjectCategory) {
		BeanUtils.copyProperties(subjectCategory, this);
	}

	@Override
	public Long getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
