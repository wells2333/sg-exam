package com.github.tangyi.api.exam.module;

import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 课程
 *
 * @author tangyi
 * @date 2018/11/8 20:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Course extends BaseEntity<Course> {

    /**
     * 课程名称
     */
    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String major;

    /**
     * 老师
     */
    private String teacher;

    /**
     * 课程描述
     */
    private String courseDescription;

    /**
     * logoId
     */
    private Long logoId;

    /**
     * logo URL
     */
    private String logoUrl;
}
