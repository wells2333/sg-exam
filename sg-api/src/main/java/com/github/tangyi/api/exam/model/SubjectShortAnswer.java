package com.github.tangyi.api.exam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 简答题
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subject_short_answer")
public class SubjectShortAnswer extends BaseSubject<SubjectShortAnswer> {

	/**
	 * 判分类型，1：自动判分，0：人工判分
	 */
	@Column(name = "judge_type")
	private Integer judgeType;

}
