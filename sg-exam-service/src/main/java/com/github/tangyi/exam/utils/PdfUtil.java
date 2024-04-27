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

package com.github.tangyi.exam.utils;

import lombok.Data;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.dromara.pdf.pdfbox.core.base.Document;
import org.dromara.pdf.pdfbox.core.ext.extractor.DocumentExtractor;
import org.dromara.pdf.pdfbox.handler.DocumentHandler;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class PdfUtil {

	private PdfUtil() {
	}

	public static Map<Integer, List<String>> extractPdfText(InputStream in) {
		Document document = DocumentHandler.getInstance().load(in);
		DocumentExtractor extractor = new DocumentExtractor(document);
		return extractor.extractText(IntStream.range(0, document.getTotalPageNumber()).toArray());
	}

	/**
	 * 简单的处理 PDF 解析的文本，解析成章节的数据结构
	 */
	public static List<Part> extractPdfTextToSection(InputStream in) {
		Map<Integer, List<String>> page2text = extractPdfText(in);
		if (MapUtils.isEmpty(page2text)) {
			return Collections.emptyList();
		}

		List<Part> sections = Lists.newArrayList();
		StringBuilder builder = new StringBuilder();
		String title = "";
		for (Map.Entry<Integer, List<String>> e : page2text.entrySet()) {
			String[] strArray = e.getValue().get(0).split("\n");
			if (strArray.length == 1) {
				title = strArray[0];
				sections.add(new Part(title, ""));
				continue;
			}

			// 这一页的第一行
			String first = strArray[0];
			// 统计后面三行的长度
			int length = strArray[1].length();
			if (strArray.length >= 3) {
				length += strArray[2].length();
			}
			if (strArray.length >= 4) {
				length += strArray[3].length();
			}
			// 比较平均长度，较小的认为是标题
			if (first.length() < (length / 3)) {
				String str = builder.toString();
				if (!str.isEmpty()) {
					sections.add(new Part(title, str));
					builder.setLength(0);
				}
				title = first;
			}

			for (String s : strArray) {
				if (StringUtils.isNotEmpty(s) && !s.equals(title) && !s.contains("第") && !s.contains("页")) {
					builder.append(s).append("\n");
				}
			}
		}

		if (!builder.isEmpty()) {
			sections.add(new Part(title, builder.toString()));
		}
		return sections;
	}

	@Data
	public static final class Part {

		private String title;
		private String content;

		public Part(String title, String content) {
			this.title = title;
			this.content = content;
		}
	}
}
