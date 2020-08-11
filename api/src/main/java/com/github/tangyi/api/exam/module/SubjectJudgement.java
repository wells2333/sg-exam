package com.github.tangyi.api.exam.module;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 判断题
 *
 * @author tangyi
 * @date 2019-07-16 12:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubjectJudgement extends BaseEntity<SubjectJudgement> {

	/**
	 * 题目分类ID
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long categoryId;

	/**
	 * 题目名称
	 */
	@NotBlank(message = "题目名称不能为空")
	private String subjectName;

	/**
	 * 参考答案
	 */
	private String answer;

	/**
	 * 分值
	 */
	@NotBlank(message = "题目分值不能为空")
	private Double score;

	/**
	 * 解析
	 */
	private String analysis;

	/**
	 * 难度等级
	 */
	private Integer level;
}
