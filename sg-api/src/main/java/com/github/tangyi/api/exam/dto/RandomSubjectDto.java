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

import lombok.Data;

@Data
public class RandomSubjectDto {

	private Long categoryId;

	private Long examinationId;

	/**
	 * 随机组题方式：0：按题目数量，1：按考试总分
	 */
	private Integer type;

	private Integer target;
}
