package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.exam.enums.SubjectType;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.service.subject.SubjectsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShortAnswerHandler extends AbstractAnswerHandler {

	public ShortAnswerHandler(SubjectsService subjectsService) {
		super(subjectsService);
	}

	@Override
	public SubjectType getSubjectType() {
		return SubjectType.SHORT_ANSWER;
	}

	@Override
	public boolean judgeRight(JudgeContext judgeContext) {
		return eq(judgeContext);
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
