package com.github.tangyi.exam.utils;

import com.github.tangyi.exam.handler.HandleResult;

import java.util.List;

/**
 * 工具类
 * @author tangyi
 * @date 2019/12/8 22:42
 */
public class HandlerUtil {

	public static HandleResult addAll(List<HandleResult> results) {
		HandleResult result = new HandleResult();
		int score = 0;
		int correctNum = 0;
		int inCorrectNum = 0;
		for (HandleResult tempResult : results) {
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
