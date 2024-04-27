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
import com.github.tangyi.exam.utils.PdfUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.util.List;

public class PdfToTextTests {

	@Test
	public void testPdfToText() throws Exception {
		String pdfPath = "";
		if (StringUtils.isEmpty(pdfPath)) {
			return;
		}

		List<PdfUtil.Part> parts = PdfUtil.extractPdfTextToSection(new FileInputStream(pdfPath));
		System.out.println(JSON.toJSONString(parts));
	}
}
