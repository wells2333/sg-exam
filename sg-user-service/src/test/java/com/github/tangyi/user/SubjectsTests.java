package com.github.tangyi.user;

import com.alibaba.fastjson.JSON;
import com.github.tangyi.common.utils.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SubjectsTests {

	@Test
	public void testImportSubject() throws Exception {
		File file = new File("");
		List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
		String subjectName = "";
		StringBuilder content = new StringBuilder();
		List<String> list = Lists.newArrayList();
		for (String line : lines) {
			if (line.contains(". ")) {
				if (StringUtils.isNotEmpty(subjectName)) {
					Map<String, Object> subject = newSubject();
					subject.put("subjectName", subjectName);
					subject.put("answer", Collections.singletonMap("answer", content.toString()));
					content.setLength(0);
					list.add(JSON.toJSONString(subject));
				}
				line = line.substring(line.indexOf(".") + 1).trim();
				subjectName = line;
			} else {
				content.append(line);
			}
		}
		System.out.println(list);
	}

	public Map<String, Object> newSubject() {
		Map<String, Object> subject = Maps.newHashMap();
		subject.put("choicesType", "0");
		subject.put("favorite", "false");
		subject.put("hasMore", "false");
		subject.put("level", "1");
		subject.put("newRecord", "true");
		subject.put("score", "5");
		// 简答题
		subject.put("type", "1");
		return subject;
	}
}
