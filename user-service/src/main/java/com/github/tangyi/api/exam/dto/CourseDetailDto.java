package com.github.tangyi.api.exam.dto;

import com.github.tangyi.api.exam.model.Course;
import lombok.Data;

import java.util.List;

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

	/**
	 * 学员数量
	 */
	private String memberCount;

	/**
	 * 用户是否已报名
	 */
	private Boolean isUserJoin;
}
