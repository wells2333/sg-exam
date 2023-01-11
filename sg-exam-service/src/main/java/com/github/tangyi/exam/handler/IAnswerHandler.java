package com.github.tangyi.exam.handler;

import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.exam.constants.MarkConstant;
import com.github.tangyi.exam.enums.SubjectType;
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
	 * @return boolean
	 */
	boolean hasOption();

	/**
	 * 统计成绩
	 * @param answers answers
	 * @return HandleResult
	 */
	AnswerHandleResult handle(List<Answer> answers);

	/**
	 * 获取题目类型
	 * @return SubjectTypeEnum
	 */
	SubjectType getSubjectType();

	/**
	 * 获取题目列表
	 * @param answers answers
	 * @return List
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
	 * @param answer answer
	 * @param subject subject
	 */
	void judgeOptionRight(Answer answer, SubjectDto subject);

	@Data
	class HandleContext {

		private final AtomicInteger rightCount = new AtomicInteger();

		private final AtomicDouble totalScore = new AtomicDouble();
	}

	@Data
	class JudgeContext {

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
