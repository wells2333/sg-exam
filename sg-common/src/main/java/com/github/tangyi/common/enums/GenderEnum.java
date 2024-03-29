package com.github.tangyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum {

	MEN("男", 0), WOMEN("女", 1);

	private final String name;
	private final Integer value;

	public static GenderEnum matchByValue(Integer value) {
		for (GenderEnum item : GenderEnum.values()) {
			if (item.value.equals(value)) {
				return item;
			}
		}
		return MEN;
	}

	public static GenderEnum matchByName(String name) {
		for (GenderEnum item : GenderEnum.values()) {
			if (item.name.equals(name)) {
				return item;
			}
		}
		return MEN;
	}
}
