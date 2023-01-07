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

@Slf4j
@AllArgsConstructor
@Component
public class SpeechAnswerHandler extends AbstractAnswerHandler {

	@Override
	public SubjectTypeEnum getSubjectType() {
		return SubjectTypeEnum.SPEECH;
	}

	@Override
	public boolean judgeRight(JudgeContext judgeContext) {
		return simpleEq(judgeContext.getAnswer(), judgeContext.getSubject());
	}

	@Override
	public void judge(HandleContext handleContext, JudgeContext judgeContext) {
		if (judgeRight(judgeContext)) {
			judgeContext.right();
		} else {
			judgeContext.wrong();
		}
		judgeContext.done();
	}
}
