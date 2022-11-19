package com.github.tangyi.api.exam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 选择题
 *
 * @author tangyi
 * @date 2018/11/8 20:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subject_choices")
public class SubjectChoices extends BaseSubject<SubjectChoices> {

	/**
	 * 题目类型，0：单选，1：多选
	 */
	@NotBlank(message = "题目类型不能为空")
	@Column(name = "choices_type")
	private Integer choicesType;

	/**
	 * 选项列表
	 */
	@Transient
	private List<SubjectOption> options;
}
