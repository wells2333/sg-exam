package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.exam.enums.SubjectType;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.service.subject.SubjectsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JudgementAnswerHandler extends AbstractAnswerHandler {

	private static final String RIGHT = "正确";
	private static final String RIGHT_FLAG = "0";
	private static final String WRONG = "错误";
	private static final String WRONG_FLAG = "1";

	public JudgementAnswerHandler(SubjectsService subjectsService) {
		super(subjectsService);
	}

	/**
	 * 转换类型，把中文转成数字
	 */
	private String convertAnswer(JudgeContext judgeContext) {
		String answer = judgeContext.getAnswer().getAnswer();
		if (RIGHT.equals(answer)) {
			answer = RIGHT_FLAG;
		} else if (WRONG.equals(answer)) {
			answer = WRONG_FLAG;
		}
		return answer;
	}

	@Override
	public SubjectType getSubjectType() {
		return SubjectType.JUDGEMENT;
	}

	@Override
	public boolean judgeRight(JudgeContext judgeContext) {
		String answer = this.convertAnswer(judgeContext);
		return judgeContext.getSubject().getAnswer().getAnswer().equalsIgnoreCase(answer);
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
