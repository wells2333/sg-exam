package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.exam.enums.SubjectType;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.service.subject.SubjectsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChoicesAnswerHandler extends AbstractAnswerHandler {

	public ChoicesAnswerHandler(SubjectsService subjectsService) {
		super(subjectsService);
	}

	@Override
	public SubjectType getSubjectType() {
		return SubjectType.CHOICES;
	}

	@Override
	public boolean hasOption() {
		return true;
	}

	@Override
	public boolean judgeRight(JudgeContext judgeContext) {
		return judgeContext.getSubject().getAnswer().getAnswer().equalsIgnoreCase(judgeContext.getAnswer().getAnswer());
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
	public void judge(HandleContext handleContext, JudgeContext judgeContext) {
		if (judgeRight(judgeContext)) {
			judgeContext.right();
		} else {
			judgeContext.wrong();
		}
		judgeContext.done();
	}
}
