package com.github.tangyi.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Servlets {

	/**
	 * 获取在不同浏览器下的下载文件的转码规则
	 * @param fileName 原名，不进行转码前名称
	 * @return String 示例 "filename=\"" + downloadName + "\"";
	 */
	public static String getDownName(HttpServletRequest request, String fileName) {
		String result = "";
		String httpUserAgent = request.getHeader("User-Agent");
		if (StringUtils.isNotEmpty(httpUserAgent)) {
			String downloadName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
			httpUserAgent = httpUserAgent.toLowerCase();
			if (httpUserAgent.contains("msie")) {
				result = "attachment;filename=\"" + downloadName + "\"";
			} // Opera 浏览器只能采用 filename*
			else if (httpUserAgent.contains("opera")) {
				result = "attachment;filename*=UTF-8''" + downloadName;
			} else if (httpUserAgent.contains("trident") || httpUserAgent.contains("edge")) {
				result = "attachment;filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
			}
			// Chrome 浏览器，只能采用 MimeUtility 编码或 ISO 编码的中文输出
			else if (httpUserAgent.contains("applewebkit")) {
				result = "attachment;filename=\"" + downloadName + "\"";
			}
			// FireFox 浏览器，可以使用 MimeUtility 或 filename*或 ISO 编码的中文输出
			else if (httpUserAgent.contains("mozilla")) {
				result = "attachment;filename*=UTF-8''" + downloadName;
			} else {
				result = "attachment;filename=\"" + downloadName + "\"";
			}
		}
		return result;
	}
}
