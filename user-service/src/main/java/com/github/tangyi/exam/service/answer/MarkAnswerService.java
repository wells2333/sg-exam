package com.github.tangyi.exam.service.answer;

import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.enums.SubmitStatusEnum;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.ExaminationRecord;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.exam.service.ExamRecordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 批改
 */
@Slf4j
@Service
@AllArgsConstructor
public class MarkAnswerService {

	private final ExamRecordService examRecordService;

	private final AnswerService answerService;

	/**
	 * 完成批改
	 */
	@Transactional
	public Boolean complete(Long recordId) {
		long start = System.currentTimeMillis();
		Answer answer = new Answer();
		answer.setExamRecordId(recordId);
		List<Answer> answers = answerService.findList(answer);
		ExaminationRecord record = examRecordService.get(recordId);
		SgPreconditions.checkNull(record, "考试记录不存在");
		if (CollectionUtils.isNotEmpty(answers)) {
			long correctNumber = answers.stream()
					.filter(tempAnswer -> tempAnswer.getAnswerType().equals(AnswerConstant.RIGHT)).count();
			// 总分
			Double score = answers.stream().mapToDouble(Answer::getScore).sum();
			record.setScore(score);
			record.setSubmitStatus(SubmitStatusEnum.CALCULATED.getValue());
			record.setCorrectNumber((int) correctNumber);
			record.setInCorrectNumber(answers.size() - record.getCorrectNumber());
			examRecordService.update(record);
			log.debug("submit done, username: {}, examinationId: {}, score: {}, time consuming: {}ms",
					record.getCreator(), record.getExaminationId(), score, System.currentTimeMillis() - start);
		}
		return Boolean.TRUE;
	}
}
