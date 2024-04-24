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
import lombok.Data;

import java.util.Date;

@Data
public class KnowledgePointDto {

	@JsonSerialize(using = ToStringSerializer.class)
	protected Long id;

	private String title;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long sort;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long sectionId;

	private String content;

	private int learnHour;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long videoId;

	private String videoName;

	private Integer contentType;

	private String videoUrl;

	private String operator;

	protected Date updateTime;

	private String speechId;
	private String speechUrl;

}
