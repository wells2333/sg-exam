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

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExaminationType {

	FORMAL("考试", 0),    //
	PRACTICE("练习", 1),    //
	QUESTIONNAIRE("问卷", 2),    //
	INTERVIEW("面试", 3);    //

	private final String name;
	private final Integer value;

	public static ExaminationType matchByValue(Integer value) {
		for (ExaminationType item : values()) {
			if (item.value.equals(value)) {
				return item;
			}
		}
		return FORMAL;
	}

	public static ExaminationType matchByName(String name) {
		for (ExaminationType item : values()) {
			if (item.name.equals(name)) {
				return item;
			}
		}
		return FORMAL;
	}
}
