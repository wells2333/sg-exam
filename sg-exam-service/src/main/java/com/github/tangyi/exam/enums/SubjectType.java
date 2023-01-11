package com.github.tangyi.exam.enums;

import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.handler.impl.*;
import com.github.tangyi.exam.service.subject.*;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

@Getter
public enum SubjectType {

	CHOICES("单选", 0, SubjectChoicesService.class, ChoicesAnswerHandler.class),

	SHORT_ANSWER("简答", 1, SubjectShortAnswerService.class, ShortAnswerHandler.class),

	JUDGEMENT("判断", 2, SubjectJudgementService.class, JudgementAnswerHandler.class),

	MULTIPLE_CHOICES("多选", 3, SubjectChoicesService.class, MultipleChoicesAnswerHandler.class),

	SPEECH("语音", 4, SubjectSpeechService.class, SpeechAnswerHandler.class),

	VIDEO("视频", 5, SubjectVideoService.class, VideoAnswerHandler.class);

	private static final Map<Integer, SubjectType> VALUE_MAP = Maps.newHashMap();

	private static final Map<String, SubjectType> NAME_MAP = Maps.newHashMap();

	static {
		for (SubjectType type : values()) {
			VALUE_MAP.put(type.value, type);
			NAME_MAP.put(type.name, type);
		}
	}

	private final String name;

	private final Integer value;

	private final Class<? extends ISubjectService> service;

	private final Class<? extends AbstractAnswerHandler> handler;

	SubjectType(String name, Integer value, Class<? extends ISubjectService> service,
			Class<? extends AbstractAnswerHandler> handler) {
		this.name = name;
		this.value = value;
		this.service = service;
		this.handler = handler;
	}

	public static SubjectType matchByValue(Integer value) {
		SubjectType type = VALUE_MAP.get(value);
		if (type == null) {
			throw new IllegalArgumentException("Invalid value: " + value);
		}
		return type;
	}

	public static SubjectType matchByName(String name) {
		SubjectType type = NAME_MAP.get(name);
		if (type == null) {
			throw new IllegalArgumentException("Invalid name: " + name);
		}
		return type;
	}
}
