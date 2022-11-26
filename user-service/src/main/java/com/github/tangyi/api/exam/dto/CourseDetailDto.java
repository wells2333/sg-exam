package com.github.tangyi.api.exam.dto;

import com.github.tangyi.api.exam.model.Course;
import lombok.Data;

import java.util.List;

/**
 *
 * @author tangyi
 * @date 2022/11/26 11:30 上午
 */
@Data
public class CourseDetailDto {

	/**
	 * 课程基本信息
	 */
	private Course course;

	/**
	 * 章节信息
	 */
	private List<CourseChapterDto> chapters;


	/**
	 * 章数
	 */
	private String chapterSize;

	/**
	 * 课时
	 */
	private String learnHour;
}
