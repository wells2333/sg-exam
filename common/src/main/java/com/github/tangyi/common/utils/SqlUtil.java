package com.github.tangyi.common.utils;

import com.google.common.io.Files;
import org.apache.commons.codec.Charsets;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mysql to h2
 * @author tangyi
 * @date 2020/8/5 4:33 下午
 */
public class SqlUtil {

	@SuppressWarnings(value={"uncheck","deprecation"})
	public static void main(String[] args) throws Exception {
		File file = new File("/Users/gaungyi.tan/gitee/microservice-exam/microservices/exam-service/src/main/resources/data.sql");
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
	 * h2的索引名必须全局唯一
	 *
	 * @param content sql建表脚本
	 * @return 替换索引名为全局唯一
	 */
	private static String uniqueKey(String content) {
		int inc = 0;
		Pattern pattern = Pattern.compile("(?<=KEY )(.*?)(?= \\()");
		Matcher matcher = pattern.matcher(content);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group() + inc++);
		}
		matcher.appendTail(sb);
		content = sb.toString();
		return content;
	}
}
