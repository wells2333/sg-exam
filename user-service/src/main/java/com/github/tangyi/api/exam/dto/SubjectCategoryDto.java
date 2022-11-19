package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.api.exam.model.SubjectCategory;
import com.github.tangyi.common.base.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

/**
 * @author tangyi
 * @date 2018/12/4 22:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubjectCategoryDto extends TreeEntity<SubjectCategoryDto> {

	public SubjectCategoryDto() {

	}

	/**
	 * 分类名称
	 */
	private String categoryName;

	/**
	 * 分类描述
	 */
	private String categoryDesc;

	/**
	 * 父分类id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

	private Integer type;

	public SubjectCategoryDto(SubjectCategory subjectCategory) {
		BeanUtils.copyProperties(subjectCategory, this);
	}

	@Override
	public Long getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
