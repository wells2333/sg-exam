package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.exam.api.constants.AnswerConstant;
import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 统计多选题
 * @author tangyi
 * @date 2020/1/19 10:02 上午
 */
@Slf4j
@AllArgsConstructor
@Component
public class MultipleChoicesAnswerHandler extends AbstractAnswerHandler {

	@Override
	public SubjectTypeEnum getSubjectType() {
		return SubjectTypeEnum.MULTIPLE_CHOICES;
	}

	@Override
	public void judge(Answer answer, SubjectDto subject, List<Double> rightScore) {
		if (StringUtils.isNotBlank(subject.getAnswer().getAnswer())) {
			boolean isRight = true;
			String[] standerAnswers = subject.getAnswer().getAnswer().split(",");
			for (String as : answer.getAnswer().split(",")) {
				if (!ArrayUtils.contains(standerAnswers, as)) {
					isRight = false;
				}
			}
			if (isRight) {
				rightScore.add(subject.getScore());
				answer.setAnswerType(AnswerConstant.RIGHT);
				answer.setScore(subject.getScore());
				answer.setMarkStatus(AnswerConstant.MARKED);
			} else {
				answer.setAnswerType(AnswerConstant.WRONG);
				answer.setScore(0.0);
				answer.setMarkStatus(AnswerConstant.MARKED);
			}
		}
	}
}
