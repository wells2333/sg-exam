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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

/**
 * 课程章 exam_course_chapter
 */
@Data
@Table(name = "exam_course_chapter")
@EqualsAndHashCode(callSuper = true)
public class ExamCourseChapter extends BaseEntity<ExamCourseChapter> {

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
     * 课程 ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "course_id")
    private Long courseId;

	/**
	 * 章描述
	 */
	@Column(name = "chapter_desc")
	private String chapterDesc;

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

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }

	public String getChapterDesc() {
		return chapterDesc;
	}

	public void setChapterDesc(String chapterDesc) {
		this.chapterDesc = chapterDesc;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
