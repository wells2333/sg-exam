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

import com.google.common.io.Files;
import org.apache.commons.codec.Charsets;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtil {

	private SqlUtil() {
	}

	@SuppressWarnings(value={"uncheck","deprecation"})
	public static void main(String[] args) throws Exception {
		File file = new File("");
		String content = Files.toString(file, Charsets.UTF_8);
		content = "SET MODE MYSQL;\n\n" + content;
		content = content.replaceAll("`", "");
		content = content.replaceAll("COLLATE.*(?=D)", "");
		content = content.replaceAll("COMMENT.*'(?=,)", "");
		content = content.replaceAll("\\).*ENGINE.*(?=;)", ")");
		content = content.replaceAll("DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", " AS CURRENT_TIMESTAMP");
		content = content.replaceAll("CHARACTER SET utf8mb4", "");
		content = content.replaceAll("CHARACTER SET utf8", "");
		content = content.replaceAll("COLLATE utf8_general_ci", "");
		content = content.replaceAll("USING BTREE", "");
		content = content.replaceAll("COLLATE utf8mb4_general_ci", "");
		content = uniqueKey(content);
		System.out.println(content);
	}

	/**
	 * h2 的索引名必须全局唯一
	 *
	 * @param content sql 建表脚本
	 * @return 替换索引名为全局唯一
	 */
	private static String uniqueKey(String content) {
		int inc = 0;
		Pattern pattern = Pattern.compile("(?<=KEY )(.*?)(?= \\()");
		Matcher matcher = pattern.matcher(content);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group() + inc++);
		}
		matcher.appendTail(sb);
		content = sb.toString();
		return content;
	}
}
