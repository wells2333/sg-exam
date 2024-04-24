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
 * 收藏、开始数量 exam_fav_start_count
 */
@Data
@Table(name = "exam_fav_start_count")
@EqualsAndHashCode(callSuper = true)
public class ExamFavStartCount extends BaseEntity<ExamFavStartCount> {

	/**
	 * 目标 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "target_id")
	private Long targetId;

	/**
	 * 开始数量
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "start_count")
	private Long startCount;

	/**
	 * 类型 ID，0：考试，1：题目，2：课程
	 */
	@Column(name = "target_type")
	private Integer targetType;

	/**
	 * 收藏数量
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "fav_count")
	private Long favCount;

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setStartCount(Long startCount) {
		this.startCount = startCount;
	}

	public Long getStartCount() {
		return startCount;
	}

	public void setTargetType(Integer targetType) {
		this.targetType = targetType;
	}

	public Integer getTargetType() {
		return targetType;
	}

	public void setFavCount(Long favCount) {
		this.favCount = favCount;
	}

	public Long getFavCount() {
		return favCount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
