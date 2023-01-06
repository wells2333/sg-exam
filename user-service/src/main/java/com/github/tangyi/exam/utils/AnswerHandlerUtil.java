package com.github.tangyi.exam.utils;

import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.exam.handler.AnswerHandleResult;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnswerHandlerUtil {

	private static final String REGEX_COMMA = "^,*|,*$";

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

	/**
	 * 替换收尾的逗号
	 * @param str str
	 * @return String
	 */
	public static String replaceComma(String str) {
		if (StringUtils.isNotBlank(str)) {
			str = str.replaceAll(REGEX_COMMA, "");
		}
		return str;
	}

	public static Map<Integer, List<Answer>> distinctAnswer(List<Answer> answers, Map<Long, Integer> typeMap) {
		Map<Integer, List<Answer>> map = Maps.newHashMap();
		map.putAll(answers.stream()
				.collect(Collectors.groupingBy(s -> typeMap.get(s.getSubjectId()), Collectors.toList())));
		return map;
	}
}
