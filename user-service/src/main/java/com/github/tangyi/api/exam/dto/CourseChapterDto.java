package com.github.tangyi.api.exam.dto;

import com.github.tangyi.api.exam.model.ExamCourseChapter;
import com.github.tangyi.api.exam.model.ExamCourseSection;
import lombok.Data;

import java.util.List;

/**
 *
 * @author tangyi
 * @date 2022/11/26 11:38 上午
 */
@Data
public class CourseChapterDto {

	/**
	 * 章信息
	 */
	private ExamCourseChapter chapter;

	/**
	 * 节列表
	 */
	private List<ExamCourseSection> sections;
}
