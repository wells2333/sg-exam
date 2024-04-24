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

package com.github.tangyi.api.user.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 反馈信息 sys_feedback
 */
@Data
@Table(name = "sys_feedback")
@EqualsAndHashCode(callSuper = true)
public class SysFeedback extends BaseEntity<SysFeedback> {

	/**
	 * 提交人
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "submitter_id")
	private Long submitterId;

	/**
	 * 提交内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 反馈状态
	 */
	@Column(name = "status")
	private Integer status;

	public void setSubmitterId(Long submitterId) {
		this.submitterId = submitterId;
	}

	public Long getSubmitterId() {
		return submitterId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
