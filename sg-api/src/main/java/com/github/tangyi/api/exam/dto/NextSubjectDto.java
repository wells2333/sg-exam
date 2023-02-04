package com.github.tangyi.api.exam.dto;

import lombok.Data;

import java.util.Set;

@Data
public class NextSubjectDto {

	private Long categoryId;

	private Long subjectId;

	/**
	 * 0：下一题，1：上一题
	 */
	private Integer nextType;

	private boolean findFav;

	private boolean isView;

	private boolean random;

	private Set<Long> realTimeIds;
}
