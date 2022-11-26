package com.github.tangyi.api.exam.dto;

import com.github.tangyi.api.exam.model.ExamCourseSection;
import lombok.Data;

/**
 * @author tangyi
 * @date 2022/11/26 1:23 下午
 */
@Data
public class CourseSectionDto {

	private ExamCourseSection section;

	private String videoUrl;
}
