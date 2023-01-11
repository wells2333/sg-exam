package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "exam_examination_subject")
public class ExaminationSubject extends BaseEntity<ExaminationSubject> {

	/**
	 * 考试ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "examination_id")
	private Long examinationId;

	/**
	 * 题目ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "subject_id")
	private Long subjectId;

	@Column(name = "sort")
	private Integer sort;
}
