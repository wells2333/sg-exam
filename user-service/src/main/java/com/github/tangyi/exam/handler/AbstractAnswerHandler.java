package com.github.tangyi.exam.handler;

import com.github.tangyi.api.exam.constants.AnswerConstant;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.Subjects;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.github.tangyi.exam.constants.MarkConstant;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.google.common.util.concurrent.AtomicDouble;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractAnswerHandler implements IAnswerHandler {

	@Override
	public boolean hasOption() {
		return false;
	}

	@Override
	public void judgeOptionRight(Answer answer, SubjectDto subject) {

	}

	@Override
	public AnswerHandleResult handle(List<Answer> answers) {
		if (CollectionUtils.isEmpty(answers)) {
			return null;
		}
		// 保存答题正确的题目分数
		AtomicInteger rightCount = new AtomicInteger();
		AtomicDouble totalScore = new AtomicDouble();
		List<SubjectDto> subjects = getSubjects(answers);
		for (Answer answer : answers) {
			AtomicBoolean judgeDone = new AtomicBoolean();
			for (SubjectDto subject : subjects) {
				if (subject.getId().equals(answer.getSubjectId())) {
					AtomicDouble score = new AtomicDouble();
					// 判分
					judge(answer, subject, score, rightCount, judgeDone);
					if (judgeDone.get() && score.get() > 0) {
						totalScore.addAndGet(score.get());
					}
					break;
				}
			}
		}
		AnswerHandleResult result = new AnswerHandleResult();
		// 记录总分、正确题目数、错误题目数
		result.setScore(totalScore.get());
		result.setCorrectNum(rightCount.get());
		result.setInCorrectNum(answers.size() - rightCount.get());
		return result;
	}

	@Override
	public List<SubjectDto> getSubjects(List<Answer> answers) {
		SubjectsService subjectsService = SpringContextHolder.getApplicationContext().getBean(SubjectsService.class);
		List<Subjects> subjects = subjectsService.findBySubjectIds(
				answers.stream().map(Answer::getSubjectId).distinct().toArray(Long[]::new));
		return subjectsService.findSubjectDtoList(subjects);
	}

	@Override
	public void marked(Answer answer, String markOperator) {
		answer.setMarkStatus(AnswerConstant.MARKED);
		answer.setMarkOperator(markOperator);
	}

	@Override
	public void judgeDone(AtomicBoolean judgeDone) {
		judgeDone.set(Boolean.TRUE);
	}

	public void setScore(SubjectDto subject, AtomicDouble score) {
		score.set(subject.getScore() == null ? 0 : subject.getScore());
	}

	public void right(Answer answer, SubjectDto subject, AtomicInteger rightCount) {
		answer.setAnswerType(AnswerConstant.RIGHT);
		answer.setScore(subject.getScore());
		rightCount.incrementAndGet();
	}

	public void wrong(Answer answer) {
		answer.setAnswerType(AnswerConstant.WRONG);
		answer.setScore(0d);
	}

	/**
	 * 简单的equals
	 */
	public boolean simpleEq(Answer answer, SubjectDto subject) {
		return StringUtils.equals(subject.getAnswer().getAnswer(), answer.getAnswer());
	}

	/**
	 * 是否自动判分
	 * @param subject subject
	 * @return boolean
	 */
	public boolean notAutoJudge(SubjectDto subject) {
		return !MarkConstant.AUTO_JUDGE.equals(subject.getJudgeType());
	}

	public void markedAndJudgeDone(Answer answer, AtomicBoolean judgeDone) {
		marked(answer, MarkConstant.DEFAULT_MARK_OPERATOR);
		judgeDone(judgeDone);
	}
}
