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
@Table(name = "exam_material_subject")
public class MaterialSubject extends BaseEntity<MaterialSubject> {

	/**
	 * 考试 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "examination_id")
	private Long examinationId;

	/**
	 * 材料 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "material_id")
	private Long materialId;

	/**
	 * 题目 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "subject_id")
	private Long subjectId;

	@Column(name = "sort")
	private Integer sort;
}
