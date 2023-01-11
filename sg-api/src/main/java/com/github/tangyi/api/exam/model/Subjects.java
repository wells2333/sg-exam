package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subjects")
public class Subjects extends BaseEntity<Subjects> {

	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "subject_id")
	private Long subjectId;

	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "category_id")
	private Long categoryId;

	/**
	 * 题目类型，0：选择题，1：简答题，2：判断题，3：多选题
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Integer type;

	@Column(name = "sort")
	private Integer sort;
}
