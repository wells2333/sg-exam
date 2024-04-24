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

package com.github.tangyi.exam.handler;

import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.exam.constants.MarkConstant;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.Data;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public interface IAnswerHandler {

	String TRUE = Boolean.TRUE.toString();
	String FALSE = Boolean.FALSE.toString();

	/**
	 * 是否为选择题
	 */
	boolean hasOption();

	/**
	 * 统计成绩
	 */
	HandlerFactory.Result handle(List<Answer> answers);

	/**
	 * 获取题目列表
	 */
	List<SubjectDto> getSubjects(List<Answer> answers);

	/**
	 * 判断逻辑
	 */
	void judge(HandleContext handleContext, JudgeContext judgeContext);

	/**
	 * 判断答题是否正确
	 */
	boolean judgeRight(JudgeContext judgeContext);

	/**
	 * 判断某个选项是否正确
	 */
	void judgeOptionRight(Answer answer, SubjectDto subject);

	@Data
	final class HandleContext {

		private final AtomicInteger rightCount = new AtomicInteger();
		private final AtomicDouble totalScore = new AtomicDouble();
	}

	@Data
	final class JudgeContext {

		private final AtomicBoolean judgeDone = new AtomicBoolean();
		private final AtomicDouble score = new AtomicDouble();
		private final HandleContext handleContext;
		private final SubjectDto subject;
		private final Answer answer;

		public JudgeContext(HandleContext handleContext, SubjectDto subject, Answer answer) {
			this.handleContext = handleContext;
			this.subject = subject;
			this.answer = answer;
		}

		public void right() {
			score.set(subject.getScore() == null ? 0 : subject.getScore());
			answer.setAnswerType(AnswerConstant.RIGHT);
			answer.setScore(subject.getScore());
			handleContext.getRightCount().incrementAndGet();
		}

		public void wrong() {
			answer.setAnswerType(AnswerConstant.WRONG);
			answer.setScore(0d);
		}

		public void done() {
			answer.setMarkStatus(AnswerConstant.MARKED);
			answer.setMarkOperator(MarkConstant.DEFAULT_MARK_OPERATOR);
			judgeDone.set(Boolean.TRUE);
		}
	}
}
