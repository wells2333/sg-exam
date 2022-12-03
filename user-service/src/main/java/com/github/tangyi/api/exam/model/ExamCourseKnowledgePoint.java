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
 * 章节知识点 exam_course_knowledge_point
 * 
 * @author tangyi
 * @date 2022-12-02
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
     * 节ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "section_id")
    private Long sectionId;

    /**
     * 知识点内容
     */
    @Column(name = "content")
    private String content;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
