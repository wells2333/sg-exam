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

import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.api.exam.model.Examination;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ExaminationDto extends Examination {

	private Course course;

	/**
	 * 封面地址
	 */
	private String imageUrl;

	/**
	 * 考试类型名称
	 */
	private String typeLabel;

	/**
	 * 是否收藏
	 */
	private boolean favorite;

	/**
	 * 参加考试人数
	 */
	private Integer joinNum;

	public static ExaminationDto of(Examination examination) {
		ExaminationDto dto = new ExaminationDto();
		BeanUtils.copyProperties(examination, dto);
		return dto;
	}

}
