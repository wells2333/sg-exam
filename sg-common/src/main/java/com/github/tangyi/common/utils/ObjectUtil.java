package com.github.tangyi.common.utils;

import java.util.Objects;

public class ObjectUtil {

	public static int intValue(Integer value) {
		return value == null ? 0 : value;
	}

	public static double doubleValue(Double value) {
		return value == null ? 0d : value;
	}

	public static Integer getInt(Integer value) {
		return value == null ? 0 : value;
	}

	public static Long getLong(Long value) {
		return value == null ? 0 : value;
	}

	public static Double getDouble(Double value) {
		return value == null ? 0 : value;
	}

	public static String toString(Object obj) {
		return Objects.toString(obj, "");
	}
}
