package com.github.tangyi.api.exam.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExamRecordDetailsDto {

	/**
	 * 考试记录
	 */
	private ExaminationRecordDto record;

	/**
	 * 答题卡
	 */
	private List<CardDto> cards;

}
