package com.github.tangyi.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FileUtil {

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
