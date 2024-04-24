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

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.Subjects;
import com.github.tangyi.exam.constants.MarkConstant;
import com.github.tangyi.exam.service.subject.SubjectsService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AtomicDouble;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractAnswerHandler implements IAnswerHandler {

	private final SubjectsService subjectsService;

	public AbstractAnswerHandler(SubjectsService subjectsService) {
		this.subjectsService = subjectsService;
	}

	@Override
	public boolean hasOption() {
		return false;
	}

	@Override
	public void judgeOptionRight(Answer answer, SubjectDto subject) {

	}

	@Override
	public HandlerFactory.Result handle(List<Answer> answers) {
		if (CollectionUtils.isEmpty(answers)) {
			return null;
		}

		HandleContext context = new HandleContext();
		AtomicInteger rightCount = context.getRightCount();
		AtomicDouble totalScore = context.getTotalScore();
		Map<Long, SubjectDto> subjectMap = this.subjectsToMap(this.getSubjects(answers));
		boolean needHumanJudge = false;
		for (Answer answer : answers) {
			SubjectDto dto = subjectMap.get(answer.getSubjectId());
			if (dto == null) {
				continue;
			}

			if (this.isAutoJudge(dto)) {
				JudgeContext c = new JudgeContext(context, dto, answer);
				this.judge(context, c);
				double score = c.getScore().get();
				if (c.getJudgeDone().get() && score > 0) {
					totalScore.addAndGet(score);
				}
			} else {
				needHumanJudge = true;
			}
		}
		HandlerFactory.Result result = new HandlerFactory.Result();
		result.setScore(totalScore.get());
		result.setCorrectNum(rightCount.get());
		result.setInCorrectNum(answers.size() - rightCount.get());
		result.setHasHumanJudgeSubject(needHumanJudge);
		return result;
	}

	@Override
	public List<SubjectDto> getSubjects(List<Answer> answers) {
		List<Subjects> subjects = subjectsService.findBySubjectIds(
				answers.stream().map(Answer::getSubjectId).distinct().toArray(Long[]::new));
		return subjectsService.findSubjectDtoList(subjects);
	}

	protected boolean eq(JudgeContext c) {
		Preconditions.checkNotNull(c.getAnswer());
		Preconditions.checkNotNull(c.getSubject());
		Preconditions.checkNotNull(c.getSubject().getAnswer());
		return StringUtils.equals(c.getAnswer().getAnswer(), c.getSubject().getAnswer().getAnswer());
	}

	private boolean isAutoJudge(SubjectDto s) {
		return s.getJudgeType() == null || MarkConstant.AUTO_JUDGE.equals(s.getJudgeType());
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
