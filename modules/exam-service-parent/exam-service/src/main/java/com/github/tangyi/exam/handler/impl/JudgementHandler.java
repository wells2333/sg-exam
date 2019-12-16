package com.github.tangyi.exam.handler.impl;

import com.github.tangyi.exam.api.constants.AnswerConstant;
import com.github.tangyi.exam.api.dto.SubjectDto;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.github.tangyi.exam.handler.BaseHandler;
import com.github.tangyi.exam.handler.HandleResult;
import com.github.tangyi.exam.service.SubjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断题
 * @author tangyi
 * @date 2019/12/8 22:01
 */
@Slf4j
@AllArgsConstructor
@Component
public class JudgementHandler implements BaseHandler {

	private final SubjectService subjectService;

	@Override
	public HandleResult handle(List<Answer> answers) {
		if (CollectionUtils.isEmpty(answers))
			return null;
		// 查找题目信息
		List<SubjectDto> subjects = subjectService.findListById(SubjectTypeEnum.JUDGEMENT.getValue(),
				answers.stream().map(Answer::getSubjectId).distinct().toArray(Long[]::new));
		// 保存答题正确的题目分数
		List<Double> rightScore = new ArrayList<>();
		answers.forEach(tempAnswer -> {
			// 题目集合
			subjects.stream()
					// 题目ID、题目答案匹配
					.filter(tempSubject -> tempSubject.getId().equals(tempAnswer.getSubjectId()) && tempSubject
							.getAnswer().getAnswer().equalsIgnoreCase(tempAnswer.getAnswer()))
					// 记录答题正确的成绩
					.findFirst().ifPresent(right -> {
				rightScore.add(right.getScore());
				tempAnswer.setAnswerType(AnswerConstant.RIGHT);
				tempAnswer.setScore(right.getScore());
				tempAnswer.setMarkStatus(AnswerConstant.MARKED);
			});
		});

		// 统计错题
		answers.forEach(tempAnswer -> {
			// 题目集合
			subjects.stream()
					// 题目ID、题目答案匹配
					.filter(tempSubject -> tempSubject.getId().equals(tempAnswer.getSubjectId()) && !tempSubject
							.getAnswer().getAnswer().equalsIgnoreCase(tempAnswer.getAnswer()))
					// 错题
					.findFirst().ifPresent(tempSubject -> {
				tempAnswer.setAnswerType(AnswerConstant.WRONG);
				tempAnswer.setScore(0.0);
				tempAnswer.setMarkStatus(AnswerConstant.MARKED);
			});
		});
		HandleResult result = new HandleResult();
		// 记录总分、正确题目数、错误题目数
		result.setScore(rightScore.stream().mapToDouble(Double::valueOf).sum());
		result.setCorrectNum(rightScore.size());
		result.setInCorrectNum(answers.size() - rightScore.size());
		return result;
	}
}
