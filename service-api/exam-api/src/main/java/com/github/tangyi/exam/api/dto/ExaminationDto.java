package com.github.tangyi.exam.api.dto;

import com.github.tangyi.exam.api.module.Course;
import com.github.tangyi.exam.api.module.Examination;

/**
 * @author tangyi
 * @date 2018/11/20 22:02
 */
public class ExaminationDto extends Examination {

    private Course course;

    public ExaminationDto() {

    }

    public ExaminationDto(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
