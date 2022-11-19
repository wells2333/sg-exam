package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author tangyi
 * @date 2022/6/25 10:56 上午
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseSubject<T> extends BaseEntity<T> {

	/**
	 * 题目分类ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "category_id")
	private Long categoryId;

	/**
	 * 题目名称
	 */
	@NotBlank(message = "题目名称不能为空")
	@Column(name = "subject_name")
	private String subjectName;

	/**
	 * 参考答案
	 */
	@Column(name = "answer")
	private String answer;

	/**
	 * 分值
	 */
	@NotBlank(message = "题目分值不能为空")
	@Column(name = "score")
	private Double score;

	/**
	 * 解析
	 */
	@Column(name = "analysis")
	private String analysis;

	/**
	 * 难度等级
	 */
	@Column(name = "level")
	private Integer level;

	@Column(name = "sort")
	private Integer sort;

	/**
	 * 语音ID
	 */
	@Column(name = "speech_id")
	private Long speechId;

}
