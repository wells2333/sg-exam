package com.github.tangyi.exam.utils;

import com.github.tangyi.exam.handler.AnswerHandleResult;

import java.util.List;

/**
 * 工具类
 * @author tangyi
 * @date 2019/12/8 22:42
 */
public class AnswerHandlerUtil {

	public static AnswerHandleResult addAll(List<AnswerHandleResult> results) {
		AnswerHandleResult result = new AnswerHandleResult();
		int score = 0;
		int correctNum = 0;
		int inCorrectNum = 0;
		for (AnswerHandleResult tempResult : results) {
			if (tempResult != null) {
				score += tempResult.getScore();
				correctNum += tempResult.getCorrectNum();
				inCorrectNum += tempResult.getInCorrectNum();
			}
		}
		result.setScore(score);
		result.setCorrectNum(correctNum);
		result.setInCorrectNum(inCorrectNum);
		return result;
	}
}
