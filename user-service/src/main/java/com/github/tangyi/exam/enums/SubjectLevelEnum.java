package com.github.tangyi.exam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 题目难度级别
 */
@Getter
@AllArgsConstructor
public enum SubjectLevelEnum {

	SIMPLE("简单", 0),

	NORMAL("一般", 1),

	DIFFICULT("略难", 2),

	MORE_DIFFICULT("非常难", 3);

	private String name;

	private Integer value;

	/**
	 * 根据类型返回具体的SubjectLevel
	 *
	 * @param value value
	 * @return SubjectLevelEnum
	 */
	public static SubjectLevelEnum matchByValue(Integer value) {
		for (SubjectLevelEnum item : values()) {
			if (item.value.equals(value)) {
				return item;
			}
		}
		return NORMAL;
	}

	/**
	 * 根据描述返回具体的SubjectLevel
	 *
	 * @param name name
	 * @return SubjectLevelEnum
	 */
	public static SubjectLevelEnum matchByName(String name) {
		for (SubjectLevelEnum item : values()) {
			if (item.name.equals(name)) {
				return item;
			}
		}
		return NORMAL;
	}
}
