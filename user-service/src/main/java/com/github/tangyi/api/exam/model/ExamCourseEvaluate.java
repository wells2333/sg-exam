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

/**
 * 课程评价 exam_course_evaluate
 *
 * @author tangyi
 * @date 2022-11-26
 */
@Data
@Table(name = "exam_course_evaluate")
@EqualsAndHashCode(callSuper = true)
public class ExamCourseEvaluate extends BaseEntity<ExamCourseEvaluate> {

	/**
	 * 评价内容
	 */
	@Column(name = "evaluate_content")
	private String evaluateContent;

	/**
	 * 用户ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "user_id")
	private Long userId;

	/**
	 * $column.columnComment
	 */
	@Column(name = "evaluate_level")
	private Integer evaluateLevel;

	/**
	 * 课程ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "course_id")
	private Long courseId;

	/**
	 * 课程名称
	 */
	@Transient
	private String courseName;

	public void setEvaluateContent(String evaluateContent) {
		this.evaluateContent = evaluateContent;
	}

	public String getEvaluateContent() {
		return evaluateContent;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setEvaluateLevel(Integer evaluateLevel) {
		this.evaluateLevel = evaluateLevel;
	}

	public Integer getEvaluateLevel() {
		return evaluateLevel;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}