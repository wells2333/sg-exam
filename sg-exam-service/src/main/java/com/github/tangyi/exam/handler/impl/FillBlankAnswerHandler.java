package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.service.subject.SubjectsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FillBlankAnswerHandler extends AbstractAnswerHandler {

	public FillBlankAnswerHandler(SubjectsService subjectsService) {
		super(subjectsService);
	}

	@Override
	public boolean hasOption() {
		return true;
	}

	@Override
	public boolean judgeRight(JudgeContext c) {
		return false;
	}

	@Override
	public void judgeOptionRight(Answer answer, SubjectDto subject) {

	}

	@Override
	public void judge(HandleContext handleContext, JudgeContext judgeContext) {

	}
}
