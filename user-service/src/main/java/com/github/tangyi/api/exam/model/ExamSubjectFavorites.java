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
 * 收藏题目 exam_subject_favorites
 * 
 * @author tangyi
 * @date 2022-08-18
 */
@Data
@Table(name = "exam_subject_favorites")
@EqualsAndHashCode(callSuper = true)
public class ExamSubjectFavorites extends BaseEntity<ExamSubjectFavorites> {

    /**
     * 题目ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "subject_id")
    private Long subjectId;

    /**
     * 用户ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "user_id")
    private Long userId;

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
