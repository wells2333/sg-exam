package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.service.subject.SubjectsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MaterialAnswerHandler extends AbstractAnswerHandler {

	public MaterialAnswerHandler(SubjectsService subjectsService) {
		super(subjectsService);
	}

	@Override
	public boolean judgeRight(JudgeContext judgeContext) {
		return eq(judgeContext);
	}

	@Override
	public void judge(HandleContext handleContext, JudgeContext judgeContext) {
		judgeContext.done();
	}
}
