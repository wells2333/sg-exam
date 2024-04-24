/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.github.tangyi.exam.utils.ExamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
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
	public boolean hasOption() {
		return true;
	}

	public void judgeOptionRight(Answer answer, SubjectDto subject) {
		String userAnswer = answer.getAnswer();
		String correctAnswer = subject.getAnswer().getAnswer();
		if (StringUtils.isNotBlank(userAnswer) && StringUtils.isNotBlank(correctAnswer)) {
			String[] userAnswers = ExamUtil.replaceComma(userAnswer).split(CommonConstant.COMMA);
			String[] correctAnswers = ExamUtil.replaceComma(correctAnswer).split(CommonConstant.COMMA);
			subject.getOptions().forEach(option -> {
				if (ArrayUtils.contains(correctAnswers, option.getOptionName())) {
					option.setRight(ArrayUtils.contains(userAnswers, option.getOptionName()) ? TRUE : FALSE);
				}
			});
		}
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
