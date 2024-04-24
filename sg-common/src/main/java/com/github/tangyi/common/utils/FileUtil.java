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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FileUtil {

	private FileUtil() {
	}

	public static String getFileNameEx(String fileName) {
		if ((fileName != null) && (fileName.length() > 0)) {
			int dot = fileName.lastIndexOf('.');
			if ((dot > -1) && (dot < (fileName.length()))) {
				fileName = fileName.substring(dot + 1);
				if (fileName.contains("?")) {
					return fileName.substring(0, fileName.indexOf("?"));
				}
				return fileName;
			}
		}
		return "";
	}

	public static String getFileNameFromUrl(String url) {
		if (StringUtils.isNotEmpty(url)) {
			int index = url.lastIndexOf("/");
			url = url.substring(index + 1);
			if (url.contains("?")) {
				return url.substring(0, url.indexOf("?"));
			}
			return url;
		}
		return "";
	}
}
