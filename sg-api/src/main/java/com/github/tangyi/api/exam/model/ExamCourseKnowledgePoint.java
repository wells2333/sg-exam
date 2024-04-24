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
 * 章节知识点 exam_course_knowledge_point
 */
@Data
@Table(name = "exam_course_knowledge_point")
@EqualsAndHashCode(callSuper = true)
public class ExamCourseKnowledgePoint extends BaseEntity<ExamCourseKnowledgePoint> {

	/**
	 * 知识点标题
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
	 * 节 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "section_id")
	private Long sectionId;

	/**
	 * 知识点内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 学习时长
	 */
	@Column(name = "learn_hour")
	private Integer learnHour;

	/**
	 * 视频 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "video_id")
	private Long videoId;

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

	private String speechId;
	private String speechUrl;
	private String videoUrl;

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public Long getSort() {
		return sort;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public Integer getLearnHour() {
		return learnHour;
	}

	public void setLearnHour(Integer learnHour) {
		this.learnHour = learnHour;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
