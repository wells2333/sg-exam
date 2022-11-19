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
 * 收藏 exam_examination_favorites
 * 
 * @author tangyi
 * @date 2022-08-18
 */
@Data
@Table(name = "exam_examination_favorites")
@EqualsAndHashCode(callSuper = true)
public class ExamExaminationFavorites extends BaseEntity<ExamExaminationFavorites> {

    /**
     * 考试ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "examination_id")
    private Long examinationId;

    /**
     * 用户ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "user_id")
    private Long userId;

    public void setExaminationId(Long examinationId) {
        this.examinationId = examinationId;
    }

    public Long getExaminationId() {
        return examinationId;
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
