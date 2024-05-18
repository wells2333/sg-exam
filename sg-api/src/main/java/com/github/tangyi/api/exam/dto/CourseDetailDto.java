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
import lombok.Data;

import java.util.List;

@Data
public class CourseDetailDto {

	/**
	 * 课程基本信息
	 */
	private Course course;

	/**
	 * 章节信息
	 */
	private List<CourseChapterDto> chapters;


	/**
	 * 章数
	 */
	private String chapterSize;

	/**
	 * 课时
	 */
	private String learnHour;

	/**
	 * 学员数量
	 */
	private String memberCount;

	/**
	 * 用户是否已报名
	 */
	private Boolean isUserJoin;

	/**
	 * 是否已经收藏
	 */
	private Boolean favorite;

	private String attachName;

	private String attachUrl;

	private List<CourseExamDto> examinations;

	@Data
	public static final class CourseExamDto {
		private Long id;
		private String examinationName;
	}
}
