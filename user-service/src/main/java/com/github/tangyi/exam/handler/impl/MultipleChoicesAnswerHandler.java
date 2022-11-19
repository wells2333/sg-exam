package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	@Override
	public boolean hasOption() {
		return true;
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
		String str = subject.getAnswer().getAnswer();
		if (StringUtils.isBlank(str)) {
			return false;
		}
		List<String> strList = Stream.of(str.split(CommonConstant.COMMA)).filter(StringUtils::isNotBlank)
				.collect(Collectors.toList());
		List<String> standList = Stream.of(answer.getAnswer().split(CommonConstant.COMMA)).collect(Collectors.toList());
		return strList.size() == standList.size()
				&& CollectionUtils.retainAll(strList, standList).size() == standList.size();
	}

	@Override
	public void judge(Answer answer, SubjectDto subject, AtomicDouble score, AtomicInteger rightCount,
			AtomicBoolean judgeDone) {
		if (StringUtils.isNotBlank(subject.getAnswer().getAnswer())) {
			if (judgeRight(answer, subject)) {
				setScore(subject, score);
				right(answer, subject, rightCount);
			} else {
				wrong(answer);
			}
			markedAndJudgeDone(answer, judgeDone);
		}
	}
}
