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

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnowledgeDto extends BaseEntity<KnowledgeDto> {

	/**
	 * 知识名称
	 */
	private String knowledgeName;

	/**
	 * 知识描述
	 */
	private String knowledgeDesc;

	/**
	 * 附件 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long attachmentId;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 附件名称
	 */
	private String attachName;

	/**
	 * 附件大小
	 */
	private String attachSize;
}
