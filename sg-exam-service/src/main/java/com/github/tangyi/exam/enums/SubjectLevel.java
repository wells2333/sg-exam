package com.github.tangyi.exam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubjectLevel {

	SIMPLE("简单", 0),

	NORMAL("一般", 1),

	DIFFICULT("略难", 2),

	MORE_DIFFICULT("非常难", 3);

	private String name;

	private Integer value;

	public static SubjectLevel matchByValue(Integer value) {
		for (SubjectLevel item : values()) {
			if (item.value.equals(value)) {
				return item;
			}
		}
		return NORMAL;
	}

	public static SubjectLevel matchByName(String name) {
		for (SubjectLevel item : values()) {
			if (item.name.equals(name)) {
				return item;
			}
		}
		return NORMAL;
	}
}
