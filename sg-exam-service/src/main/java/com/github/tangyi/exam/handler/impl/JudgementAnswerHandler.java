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
