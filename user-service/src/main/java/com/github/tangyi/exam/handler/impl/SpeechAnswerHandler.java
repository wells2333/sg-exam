package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author tangyi
 * @date 2022/11/9 10:27 下午
 */
@Slf4j
@AllArgsConstructor
@Component
public class SpeechAnswerHandler extends AbstractAnswerHandler {

	@Override
	public SubjectTypeEnum getSubjectType() {
		return SubjectTypeEnum.SPEECH;
	}

	@Override
	public boolean judgeRight(Answer answer, SubjectDto subject) {
		return simpleEq(answer, subject);
	}

	@Override
	public void judge(Answer answer, SubjectDto subject, AtomicDouble score, AtomicInteger rightCount,
			AtomicBoolean judgeDone) {
		if (notAutoJudge(subject)) {
			return;
		}
		if (judgeRight(answer, subject)) {
			setScore(subject, score);
			right(answer, subject, rightCount);
		} else {
			wrong(answer);
		}
		markedAndJudgeDone(answer, judgeDone);
	}
}
