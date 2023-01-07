package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class VideoAnswerHandler extends AbstractAnswerHandler {

	@Override
	public SubjectTypeEnum getSubjectType() {
		return SubjectTypeEnum.VIDEO;
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
