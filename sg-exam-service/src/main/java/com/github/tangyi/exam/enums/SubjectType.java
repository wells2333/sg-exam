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

package com.github.tangyi.exam.enums;

import com.github.tangyi.api.exam.model.SubjectMaterial;
import com.github.tangyi.exam.handler.AbstractAnswerHandler;
import com.github.tangyi.exam.handler.impl.*;
import com.github.tangyi.exam.service.subject.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;

import java.util.Map;

@Getter
public enum SubjectType {

	CHOICES("单选", 0, SubjectChoicesService.class, ChoicesAnswerHandler.class),    //
	SHORT_ANSWER("简答", 1, SubjectShortAnswerService.class, ShortAnswerHandler.class),    //
	JUDGEMENT("判断", 2, SubjectJudgementService.class, JudgementAnswerHandler.class),    //
	MULTIPLE_CHOICES("多选", 3, SubjectChoicesService.class, MultipleChoicesAnswerHandler.class), //
	FILL_BLANK("填空", 4, SubjectFillBlankService.class, FillBlankAnswerHandler.class),    //
	MATERIAL("材料", 5,SubjectMaterialService.class, MaterialAnswerHandler.class);    //
	private static final Int2ObjectOpenHashMap<SubjectType> VALUE_MAP = new Int2ObjectOpenHashMap<>();
	private static final Map<String, SubjectType> NAME_MAP = Maps.newHashMap();

	static {
		for (SubjectType type : values()) {
			VALUE_MAP.put(type.value, type);
			NAME_MAP.put(type.name, type);
		}
	}

	private final String name;
	private final int value;
	private final Class<? extends ISubjectService> service;
	private final Class<? extends AbstractAnswerHandler> handler;

	SubjectType(String name, int value, Class<? extends ISubjectService> service,
			Class<? extends AbstractAnswerHandler> handler) {
		this.name = name;
		this.value = value;
		this.service = service;
		this.handler = handler;
	}

	public static SubjectType matchByValue(int value) {
		SubjectType type = VALUE_MAP.get(value);
		Preconditions.checkNotNull(type);
		return type;
	}

	public static SubjectType matchByName(String name) {
		SubjectType type = NAME_MAP.get(name);
		Preconditions.checkNotNull(type);
		return type;
	}
}
