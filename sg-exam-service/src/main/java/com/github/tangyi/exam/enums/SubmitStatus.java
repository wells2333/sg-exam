package com.github.tangyi.exam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 提交状态
 */
@Getter
@AllArgsConstructor
public enum SubmitStatus {

	SUBMITTED("已提交", 0), UN_SUBMITTED("未提交", 1);

	private String name;

	private Integer value;

	public static SubmitStatus matchByValue(Integer value) {
		for (SubmitStatus item : values()) {
			if (item.value.equals(value)) {
				return item;
			}
		}
		return UN_SUBMITTED;
	}

	public static SubmitStatus matchByName(String name) {
		for (SubmitStatus item : values()) {
			if (item.name.equals(name)) {
				return item;
			}
		}
		return UN_SUBMITTED;
	}
}
