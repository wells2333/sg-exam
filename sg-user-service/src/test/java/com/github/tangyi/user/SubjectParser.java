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

package com.github.tangyi.user;

import com.alibaba.fastjson.JSON;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.exam.enums.SubjectLevel;
import com.github.tangyi.exam.enums.SubjectType;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubjectParser {

	public static void main(String[] args) throws Exception {
		List<SubjectDto> result = Lists.newArrayList();
		File file = new File("");
		List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
		SubjectDto dto = null;
		int cnt = 0;
		List<SubjectOption> tempOptions = null;
		for (String line : lines) {
			try {
				if (StringUtils.isEmpty(line)) {
					continue;
				}

				if (dto == null) {
					cnt++;
					dto = newDto();
					dto.setSort(cnt);
					tempOptions = null;
				}

				String prefix = cnt + "、";
				if (line.startsWith(prefix)) {
					String subjectName = line.replace(prefix, "");
					if (subjectName.contains("(") && subjectName.contains(")")) {
						String answer = subjectName.substring(subjectName.lastIndexOf("("));
						subjectName = subjectName.replace(answer, "");
						answer = answer.replace("(", "").replace(")", "");
						Answer a = new Answer();
						a.setAnswer(answer);
						dto.setAnswer(a);
					} else if (subjectName.contains("（") && subjectName.contains("）")) {
						String answer = subjectName.substring(subjectName.lastIndexOf("（"));
						subjectName = subjectName.replace(answer, "");
						answer = answer.replace("（", "").replace("）", "");
						Answer a = new Answer();
						a.setAnswer(answer);
						dto.setAnswer(a);
					}
					dto.setSubjectName(subjectName);
					continue;
				}

				// 多个选项在同一行
				if (line.contains("A、") && line.contains("B、") && line.contains("C、") && line.contains("D、")) {
					if (StringUtils.isEmpty(dto.getSubjectName())) {
						System.out.println("subjectName is empty");
					}

					List<String> strList = Lists.newArrayList();
					int b = line.indexOf("B、");
					int c = line.indexOf("C、");
					int d = line.indexOf("D、");

					strList.add(line.substring(0, b));
					strList.add(line.substring(b, c));
					strList.add(line.substring(c, d));
					strList.add(line.substring(d));

					int i = 1;
					List<SubjectOption> options = Lists.newArrayListWithExpectedSize(4);
					for (String s : strList) {
						String[] oo = s.split("、");
						SubjectOption o = new SubjectOption();
						o.setOptionName(oo[0]);
						o.setOptionContent(oo[1].trim());
						o.setSort(i++);
						options.add(o);
					}
					dto.setOptions(options);
					result.add(dto);
					dto = null;
				} else {
					if (StringUtils.isEmpty(dto.getSubjectName())) {
						System.out.println("subjectName is empty");
					}
					if (tempOptions == null) {
						tempOptions = Lists.newArrayListWithExpectedSize(4);
					}
					String[] oo = line.split("、");
					SubjectOption o = new SubjectOption();
					o.setOptionName(oo[0]);
					o.setOptionContent(oo[1].trim());
					tempOptions.add(o);
					if (line.startsWith("A、")) {
						o.setSort(1);
					} else if (line.startsWith("B、")) {
						o.setSort(2);
					} else if (line.startsWith("C、")) {
						o.setSort(3);
					} else if (line.startsWith("D、")) {
						o.setSort(4);
						tempOptions.add(o);
						dto.setOptions(tempOptions);
						result.add(dto);
						dto = null;
					}
				}
			} catch (Exception e) {
				System.out.println(line);
				e.printStackTrace();
			}
		}

		System.out.println(JSON.toJSONString(result));
//		for (SubjectDto d : result) {
//			System.out.println(d.getSubjectName());
//			System.out.println(d.getAnswer().getAnswer());
//		}
	}

	private static SubjectDto newDto() {
		SubjectDto dto = new SubjectDto();
		dto.setType(SubjectType.CHOICES.getValue());
		dto.setChoicesType(0);
		dto.setScore(5.0);
		dto.setLevel(SubjectLevel.NORMAL.getValue());
		return dto;
	}
}
