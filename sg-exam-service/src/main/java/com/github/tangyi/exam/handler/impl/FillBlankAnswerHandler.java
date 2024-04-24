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

import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.service.subject.SubjectsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FillBlankAnswerHandler extends AbstractAnswerHandler {

	private static final String SPLITTER = "\n";

	public FillBlankAnswerHandler(SubjectsService subjectsService) {
		super(subjectsService);
	}

	@Override
	public boolean judgeRight(JudgeContext c) {
		return false;
	}

	@Override
	public void judge(HandleContext handleContext, JudgeContext c) {
		// 用户提交的答案
		String[] userAnswers = StringUtils.split(c.getAnswer().getAnswer(), SPLITTER);
		// 参考答案
		String[] referenceAnswer = StringUtils.split(c.getSubject().getAnswer().getAnswer(), SPLITTER);

		// 判断每个填空是否答对
		int rightCnt = 0;
		double subjectScore = c.getSubject().getScore() == null ? 0 : c.getSubject().getScore();
		double totalScore = 0;
		for (int i = 0; i < referenceAnswer.length; i++) {
			if (userAnswers.length > i && StringUtils.equals(referenceAnswer[i], userAnswers[i])) {
				rightCnt++;
				totalScore += subjectScore;
			}
		}

		c.getScore().set(totalScore);
		c.getAnswer().setScore(totalScore);
		// 全部答对
		if (rightCnt == referenceAnswer.length) {
			c.getAnswer().setAnswerType(AnswerConstant.RIGHT);
			handleContext.getRightCount().incrementAndGet();
		} else {
			c.getAnswer().setAnswerType(AnswerConstant.WRONG);
			c.getAnswer().setScore(totalScore);
		}
		c.done();
	}
}
