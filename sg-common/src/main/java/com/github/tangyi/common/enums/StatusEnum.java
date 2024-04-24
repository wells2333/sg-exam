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

package com.github.tangyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {

	ENABLE("启用", 0), DISABLE("禁用", 1);

	private final String name;
	private final Integer value;

	public static StatusEnum matchByValue(Integer value) {
		for (StatusEnum item : StatusEnum.values()) {
			if (item.value.equals(value)) {
				return item;
			}
		}
		return ENABLE;
	}

	public static StatusEnum matchByName(String name) {
		for (StatusEnum item : StatusEnum.values()) {
			if (item.name.equals(name)) {
				return item;
			}
		}
		return ENABLE;
	}
}
