package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@AllArgsConstructor
@Component
public class ChoicesAnswerHandler extends AbstractAnswerHandler {

	@Override
	public SubjectTypeEnum getSubjectType() {
		return SubjectTypeEnum.CHOICES;
	}

	@Override
	public boolean hasOption() {
		return true;
	}

	@Override
	public boolean judgeRight(Answer answer, SubjectDto subject) {
		return subject.getAnswer().getAnswer().equalsIgnoreCase(answer.getAnswer());
	}

	/**
	 * 判断选项是否正确
	 */
	@Override
	public void judgeOptionRight(Answer answer, SubjectDto subject) {
		String userAnswer = answer.getAnswer();
		String answerStr = subject.getAnswer().getAnswer();
		if (StringUtils.isNotBlank(userAnswer)) {
			subject.getOptions().forEach(option -> {
				if (userAnswer.equals(option.getOptionName())) {
					option.setRight(answerStr.equals(option.getOptionName()) ? TRUE : FALSE);
				}
			});
		}
	}

	@Override
	public void judge(Answer answer, SubjectDto subject, AtomicDouble score, AtomicInteger rightCount,
			AtomicBoolean judgeDone) {
		if (judgeRight(answer, subject)) {
			setScore(subject, score);
			right(answer, subject, rightCount);
		} else {
			wrong(answer);
		}
		markedAndJudgeDone(answer, judgeDone);
	}
}
