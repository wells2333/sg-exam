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
 * 课程节 exam_course_section
 */
@Data
@Table(name = "exam_course_section")
@EqualsAndHashCode(callSuper = true)
public class ExamCourseSection extends BaseEntity<ExamCourseSection> {

	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 序号
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "sort")
	private Long sort;

	/**
	 * 章 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "chapter_id")
	private Long chapterId;

	/**
	 * 时长
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "learn_hour")
	private Long learnHour;

	/**
	 * 视频 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "video_id")
	private Long videoId;

	/**
	 * 节描述
	 */
	@Column(name = "section_desc")
	private String sectionDesc;

	/**
	 * 视频名称
	 */
	@Column(name = "video_name")
	private String videoName;

	/**
	 * 内容类型
	 */
	@Column(name = "content_type")
	private Integer contentType;

	/**
	 * 节内容
	 */
	@Column(name = "content")
	private String content;

	private String speechId;
	private String speechUrl;
	private String videoUrl;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
