package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.exam.enums.SubjectType;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.service.subject.SubjectsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class MultipleChoicesAnswerHandler extends AbstractAnswerHandler {

	public MultipleChoicesAnswerHandler(SubjectsService subjectsService) {
		super(subjectsService);
	}

	@Override
	public SubjectType getSubjectType() {
		return SubjectType.MULTIPLE_CHOICES;
	}

	@Override
	public boolean hasOption() {
		return true;
	}

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
	public boolean judgeRight(JudgeContext judgeContext) {
		String str = judgeContext.getSubject().getAnswer().getAnswer();
		if (StringUtils.isBlank(str)) {
			return false;
		}
		List<String> strList = Stream.of(str.split(CommonConstant.COMMA)).filter(StringUtils::isNotBlank)
				.collect(Collectors.toList());
		List<String> standList = Stream.of(judgeContext.getAnswer().getAnswer().split(CommonConstant.COMMA))
				.collect(Collectors.toList());
		return strList.size() == standList.size()
				&& CollectionUtils.retainAll(strList, standList).size() == standList.size();
	}

	@Override
	public void judge(HandleContext handleContext, JudgeContext judgeContext) {
		if (StringUtils.isBlank(judgeContext.getSubject().getAnswer().getAnswer())) {
			return;
		}
		if (judgeRight(judgeContext)) {
			judgeContext.right();
		} else {
			judgeContext.wrong();
		}
		judgeContext.done();
	}
}
