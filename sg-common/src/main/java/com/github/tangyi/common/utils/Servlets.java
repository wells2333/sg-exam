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

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Servlets {

	private Servlets() {
	}

	/**
	 * 获取在不同浏览器下的下载文件的转码规则
	 * @param fileName 原名，不进行转码前名称
	 * @return String 示例 "filename=\"" + downloadName + "\"";
	 */
	public static String getDownName(HttpServletRequest req, String fileName) {
		String result = "";
		String agent = req.getHeader("User-Agent");
		if (StringUtils.isNotEmpty(agent)) {
			String downloadName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
			agent = agent.toLowerCase();
			if (agent.contains("msie")) {
				result = "attachment;filename=\"" + downloadName + "\"";
			} else if (agent.contains("opera")) {
				// Opera 浏览器只能采用 filename*
				result = "attachment;filename*=UTF-8''" + downloadName;
			} else if (agent.contains("trident") || agent.contains("edge")) {
				result = "attachment;filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
			} else if (agent.contains("applewebkit")) {
				// Chrome 浏览器，只能采用 MimeUtility 编码或 ISO 编码的中文输出
				result = "attachment;filename=\"" + downloadName + "\"";
			} else if (agent.contains("mozilla")) {
				// FireFox 浏览器，可以使用 MimeUtility 或 filename*或 ISO 编码的中文输出
				result = "attachment;filename*=UTF-8''" + downloadName;
			} else {
				result = "attachment;filename=\"" + downloadName + "\"";
			}
		}
		return result;
	}
}
