package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.api.exam.model.SubjectCategory;
import com.github.tangyi.common.base.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubjectCategoryDto extends TreeEntity<SubjectCategoryDto> {

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

	private Integer subjectCnt;

	private Integer status;

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
