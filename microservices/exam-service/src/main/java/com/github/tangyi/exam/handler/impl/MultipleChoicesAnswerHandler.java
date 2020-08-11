package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.module.Answer;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

	/**
	 * 判断选项是否正确
	 *
	 * @param answer  answer
	 * @param subject subject
	 * @author tangyi
	 * @date 2020/02/19 23:23
	 */
	public void judgeOptionRight(Answer answer, SubjectDto subject) {
//		String userAnswer = answer.getAnswer();
//		String correctAnswer = subject.getAnswer().getAnswer();
//		if (StringUtils.isNotBlank(userAnswer) && StringUtils.isNotBlank(correctAnswer)) {
//			String[] userAnswers = AnswerHandlerUtil.replaceComma(userAnswer).split(CommonConstant.COMMA);
//			String[] correctAnswers = AnswerHandlerUtil.replaceComma(correctAnswer).split(CommonConstant.COMMA);
//			subject.getOptions().forEach(option -> {
//				if (ArrayUtils.contains(correctAnswers, option.getOptionName())) {
//					option.setRight(ArrayUtils.contains(userAnswers, option.getOptionName()) ? TRUE : FALSE);
//				}
//			});
//		}
	}

	@Override
	public boolean judgeRight(Answer answer, SubjectDto subject) {
//		String[] correctAnswers = AnswerHandlerUtil.replaceComma(subject.getAnswer().getAnswer()).split(CommonConstant.COMMA);
//		for (String as : answer.getAnswer().split(CommonConstant.COMMA)) {
//			if (!ArrayUtils.contains(correctAnswers, as)) {
//				return false;
//			}
//		}
		return true;
	}

	@Override
	public void judge(Answer answer, SubjectDto subject, List<Double> rightScore) {
		if (StringUtils.isNotBlank(subject.getAnswer().getAnswer())) {
			if (judgeRight(answer, subject)) {
				rightScore.add(subject.getScore());
				answer.setAnswerType(AnswerConstant.RIGHT);
				answer.setScore(subject.getScore());
			} else {
				answer.setAnswerType(AnswerConstant.WRONG);
				answer.setScore(0.0);
			}
			answer.setMarkStatus(AnswerConstant.MARKED);
		}
	}
}
