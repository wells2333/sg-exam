package com.github.tangyi.api.exam.dto;

import lombok.Data;

@Data
public class RandomSubjectDto {

	private Long categoryId;
	
	private Long examinationId;

	private Integer subjectCount;
}
