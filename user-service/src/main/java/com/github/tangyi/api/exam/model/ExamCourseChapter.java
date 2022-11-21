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
 * 
 * @author tangyi
 * @date 2022-11-21
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
     * 课程ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "course_id")
    private Long courseId;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
