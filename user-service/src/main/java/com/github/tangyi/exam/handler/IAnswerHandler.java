package com.github.tangyi.exam.handler;

import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.exam.enums.SubjectTypeEnum;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 统计成绩
 * @author tangyi
 * @date 2019/12/8 9:56 下午
 */
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
	SubjectTypeEnum getSubjectType();

	/**
	 * 获取题目列表
	 * @param answers answers
	 * @return List
	 */
	List<SubjectDto> getSubjects(List<Answer> answers);

	/**
	 * 判断逻辑
	 * @param answer answer
	 * @param subject subject
	 * @param rightCount rightCount
	 */
	void judge(Answer answer, SubjectDto subject, AtomicDouble score, AtomicInteger rightCount,
			AtomicBoolean judgeDone);

	/**
	 * 判断答题是否正确
	 * @param answer answer
	 * @param subject subject
	 */
	boolean judgeRight(Answer answer, SubjectDto subject);

	/**
	 * 判断某个选项是否正确
	 * @param answer answer
	 * @param subject subject
	 */
	void judgeOptionRight(Answer answer, SubjectDto subject);

	void marked(Answer answer, String markOperator);

	/**
	 * 是否完成判分
	 * @param judgeDone judgeDone
	 */
	void judgeDone(AtomicBoolean judgeDone);

}
