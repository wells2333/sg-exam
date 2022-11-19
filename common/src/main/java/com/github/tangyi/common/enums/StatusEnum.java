package com.github.tangyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举
 * @author tangyi
 * @date 2019/12/10 14:32
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

	ENABLE("启用", 0), DISABLE("禁用", 1);

	private String name;

	private Integer value;

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
