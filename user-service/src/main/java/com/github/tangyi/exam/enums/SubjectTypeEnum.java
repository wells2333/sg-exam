package com.github.tangyi.exam.enums;

import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.handler.impl.*;
import com.github.tangyi.exam.service.subject.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 题目类型枚举
 */
@Getter
@AllArgsConstructor
public enum SubjectTypeEnum {

	CHOICES("单选", 0, SubjectChoicesService.class, ChoicesAnswerHandler.class),

	SHORT_ANSWER("简答", 1, SubjectShortAnswerService.class, ShortAnswerHandler.class),

	JUDGEMENT("判断", 2, SubjectJudgementService.class, JudgementAnswerHandler.class),

	MULTIPLE_CHOICES("多选", 3, SubjectChoicesService.class, MultipleChoicesAnswerHandler.class),

	SPEECH("语音", 4, SubjectSpeechService.class, SpeechAnswerHandler.class),

	VIDEO("视频", 5, SubjectVideoService.class, VideoAnswerHandler.class);

	private String name;

	private Integer value;

	private Class<? extends ISubjectService> service;

	private Class<? extends AbstractAnswerHandler> handler;

	/**
	 * 根据类型返回具体的SubjectType
	 *
	 * @param value value
	 * @return SubjectType
	 */
	public static SubjectTypeEnum matchByValue(Integer value) {
		for (SubjectTypeEnum item : values()) {
			if (item.value.equals(value)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 根据描述返回具体的SubjectType
	 *
	 * @param name name
	 * @return SubjectType
	 */
	public static SubjectTypeEnum matchByName(String name) {
		for (SubjectTypeEnum item : values()) {
			if (item.name.equals(name)) {
				return item;
			}
		}
		return CHOICES;
	}
}
