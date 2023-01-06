package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 选择题的选项
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subject_option")
public class SubjectOption extends BaseEntity<SubjectOption> {

	/**
	 * 选择题ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "subject_choices_id")
	private Long subjectChoicesId;

	/**
	 * 选项名称
	 */
	@Column(name = "option_name")
	private String optionName;

	/**
	 * 选项内容
	 */
	@Column(name = "option_content")
	private String optionContent;

	/**
	 * 是否正确
	 */
	@Column(name = "right")
	private String right;

	@Column(name = "sort")
	private Integer sort;
}
