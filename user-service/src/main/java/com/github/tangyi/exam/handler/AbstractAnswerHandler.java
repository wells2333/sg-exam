package com.github.tangyi.exam.handler;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.Subjects;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.github.tangyi.exam.constants.MarkConstant;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AtomicDouble;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
		HandleContext handleContext = new HandleContext();
		AtomicInteger rightCount = handleContext.getRightCount();
		AtomicDouble totalScore = handleContext.getTotalScore();
		Map<Long, SubjectDto> subjectMap = subjectsToMap(getSubjects(answers));
		for (Answer answer : answers) {
			SubjectDto subject = subjectMap.get(answer.getSubjectId());
			if (subject != null && notAutoJudge(subject)) {
				JudgeContext judgeContext = new JudgeContext(handleContext, subject, answer);
				judge(handleContext, judgeContext);
				double score = judgeContext.getScore().get();
				if (judgeContext.getJudgeDone().get() && score > 0) {
					totalScore.addAndGet(score);
				}
			}
		}
		AnswerHandleResult result = new AnswerHandleResult();
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

	/**
	 * 简单的equals
	 */
	protected boolean simpleEq(Answer answer, SubjectDto subject) {
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

	private Map<Long, SubjectDto> subjectsToMap(List<SubjectDto> subjects) {
		if (CollectionUtils.isEmpty(subjects)) {
			return Collections.emptyMap();
		}
		Map<Long, SubjectDto> map = Maps.newHashMapWithExpectedSize(subjects.size());
		for (SubjectDto subject : subjects) {
			map.put(subject.getId(), subject);
		}
		return map;
	}
}
