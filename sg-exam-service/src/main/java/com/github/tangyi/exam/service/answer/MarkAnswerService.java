/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
		long startMs = System.currentTimeMillis();
		Answer answer = new Answer();
		answer.setExamRecordId(recordId);
		List<Answer> answers = answerService.findList(answer);
		ExaminationRecord record = examRecordService.get(recordId);
		SgPreconditions.checkNull(record, "The examination record does not exists.");
		if (CollectionUtils.isNotEmpty(answers)) {
			long correctNumber = answers.stream().filter(t -> t.getAnswerType().equals(AnswerConstant.RIGHT)).count();
			// 总分
			double score = answers.stream().mapToDouble(Answer::getScore).sum();
			record.setScore(score);
			record.setSubmitStatus(SubmitStatusEnum.CALCULATED.getValue());
			record.setCorrectNumber((int) correctNumber);
			record.setInCorrectNumber(answers.size() - record.getCorrectNumber());
			examRecordService.update(record);
			log.debug("Submit done, username: {}, examinationId: {}, score: {}, time consuming: {}ms",
					record.getCreator(), record.getExaminationId(), score, System.currentTimeMillis() - startMs);
		}
		return Boolean.TRUE;
	}
}
