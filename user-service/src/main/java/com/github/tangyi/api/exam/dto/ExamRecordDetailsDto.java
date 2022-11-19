package com.github.tangyi.api.exam.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * @author tangyi
 * @date 2022/8/3 10:33 下午
 */
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
