package com.github.tangyi.api.exam.dto;

import com.github.tangyi.api.exam.model.ExamCourseChapter;
import com.github.tangyi.api.exam.model.ExamCourseSection;
import lombok.Data;

import java.util.List;

@Data
public class CourseChapterDto {

	/**
	 * 章信息
	 */
	private ExamCourseChapter chapter;

	/**
	 * 节列表
	 */
	private List<CourseSectionDto> sections;
}
