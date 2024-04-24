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

package com.github.tangyi.common.utils;

public class EnvUtils {

	private EnvUtils() {
	}

	public static String getValue(String key, String defaultVal) {
		try {
			String value = getValue(key);
			if (value == null) {
				return defaultVal;
			} else {
				return value;
			}
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static String getValue(String key) {
		// 系统 -D
		String val = System.getProperty(key);
		if (val != null) {
			return val;
		}
		return System.getenv(key);
	}

	public static int getInt(String key, int defaultVal) {
		String val = getValue(key);
		return val == null ? defaultVal : Integer.parseInt(val);
	}

	public static long getLong(String key, long defaultVal) {
		String val = getValue(key);
		return val == null ? defaultVal : Long.parseLong(val);
	}
}
