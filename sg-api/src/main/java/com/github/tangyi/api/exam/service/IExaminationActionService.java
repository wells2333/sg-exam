package com.github.tangyi.api.exam.service;

import com.github.tangyi.api.exam.dto.AnswerDto;
import com.github.tangyi.api.exam.dto.ExamRecordDetailsDto;
import com.github.tangyi.api.exam.dto.StartExamDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.ExaminationRecord;

import java.util.List;

public interface IExaminationActionService {

	StartExamDto start(ExaminationRecord examRecord);

	StartExamDto start(Long userId, String identifier, Long examinationId, String tenantCode);

	StartExamDto anonymousUserStart(Long examinationId, String identifier);

	/**
	 * 提交答卷，自动统计选择题得分
	 */
	Boolean submit(Long recordId, String operator, String tenantCode);

	boolean submitAsync(Answer answer);

	Boolean submitAll(List<AnswerDto> answers);

	/**
	 * 移动端提交答题
	 */
	boolean anonymousUserSubmit(Long examinationId, String identifier, List<SubjectDto> dtos);

	/**
	 * 成绩详情
	 */
	ExamRecordDetailsDto details(Long id);
}
