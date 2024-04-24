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

package com.github.tangyi.api.exam.enums;

public enum SubmitStatusEnum {

	UNKNOWN("未知", -1), NOT_SUBMITTED("未提交", 0), SUBMITTED("已提交", 1), CALCULATE("正在统计", 2), CALCULATED(
			"统计完成", 3);

	private String name;

	private Integer value;

	SubmitStatusEnum(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public static SubmitStatusEnum match(Integer value, SubmitStatusEnum defaultValue) {
		if (value != null) {
			for (SubmitStatusEnum item : SubmitStatusEnum.values()) {
				if (item.value.equals(value)) {
					return item;
				}
			}
		}
		return defaultValue;
	}

	public static SubmitStatusEnum matchByName(String name, SubmitStatusEnum defaultValue) {
		if (name != null) {
			for (SubmitStatusEnum item : SubmitStatusEnum.values()) {
				if (item.name.equals(name)) {
					return item;
				}
			}
		}
		return defaultValue;
	}
}
