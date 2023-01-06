package com.github.tangyi.common.utils;

import java.util.Objects;

public class ObjectUtil {

	/**
	 * 将字符串转换为double,如果字符串为空或者null，则自动转换为0.0。
	 *
	 * @param toConvert 需要转换的字符串
	 * @return double
	 */
	public static double obj2Double(Object toConvert) {
		if (((toConvert instanceof Double))) {
			return (Double) toConvert;
		}
		double d = 0.0D;
		try {
			d = Double.parseDouble(String.valueOf(toConvert));
		} catch (Exception ignored) {
		}
		return d;
	}

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
