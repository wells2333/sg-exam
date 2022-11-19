package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

/**
 * 课程
 *
 * @author tangyi
 * @date 2018/11/8 20:43
 */
@Data
@Table(name = "exam_course")
@EqualsAndHashCode(callSuper = true)
public class Course extends BaseEntity<Course> {

	/**
	 * 课程名称
	 */
	@NotBlank(message = "课程名称不能为空")
	@Column(name = "course_name")
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
	@Column(name = "course_description")
	private String courseDescription;

	/**
	 * logoId
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "image_id")
	private Long imageId;

	/**
	 * image URL
	 */
	@Transient
	private String imageUrl;
}
