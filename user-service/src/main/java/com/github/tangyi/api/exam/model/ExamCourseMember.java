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
 * 课程学员 exam_course_member
 * 
 * @author tangyi
 * @date 2022-11-27
 */
@Data
@Table(name = "exam_course_member")
@EqualsAndHashCode(callSuper = true)
public class ExamCourseMember extends BaseEntity<ExamCourseMember> {

    /**
     * 课程ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "course_id")
    private Long courseId;

    /**
     * 用户ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "user_id")
    private Long userId;

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
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
