package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * 题目分类
 *
 * @author tangyi
 * @date 2018-12-04 11:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_subject_category")
public class SubjectCategory extends BaseEntity<SubjectCategory> {

	/**
	 * 分类名称
	 */
	@NotBlank(message = "分类名称不能为空")
	@Column(name = "category_name")
	private String categoryName;

	/**
	 * 分类描述
	 */
	@Column(name = "category_desc")
	private String categoryDesc;

	/**
	 * 父分类id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "parent_id")
	private Long parentId;

	/**
	 * 排序号
	 */
	@Column(name = "sort")
	private Integer sort;

	/**
	 * 类型: 0-私共,1-公有
	 */
	@Column(name = "type")
	private Integer type;
}
