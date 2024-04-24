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
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CompareLocalsTests {

	@Test
	public void testCompareLocals() throws IOException {
		URL url = CompareLocalsTests.class.getResource("/test.env");
		Assertions.assertNotNull(url);
		String path = url.getPath();
		Assertions.assertNotNull(path);
		File file = new File(path);
		File projectRoot = file.getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
		Assertions.assertNotNull(projectRoot);
		String langPah = projectRoot.getPath() + "/frontend/sg-exam-app/src/locales/lang";
		File enFile = new File(langPah + "/en/en.js");
		File zhFile = new File(langPah + "/zh-CN/zh.js");
		Assertions.assertNotNull(enFile);
		Assertions.assertNotNull(zhFile);

		String en = FileUtils.readFileToString(enFile, StandardCharsets.UTF_8);
		String zh = FileUtils.readFileToString(zhFile, StandardCharsets.UTF_8);
		Assertions.assertNotNull(en);
		Assertions.assertNotNull(zh);

		en = en.replace("const en =", "").replace("export default en", "").trim();
		zh = zh.replace("const zh =", "").replace("export default zh", "").trim();

		JSONObject enObj = JSON.parseObject(en);
		JSONObject zhObj = JSON.parseObject(zh);

		compareJSONObject(zhObj, enObj);
	}

	private void compareJSONObject(JSONObject one, JSONObject two) {
		if (one == null || two == null) {
			return;
		}
		for (Map.Entry<String, Object> entry : one.entrySet()) {
			if (!two.containsKey(entry.getKey())) {
				System.out.println(entry.getKey());
			}
			if (entry.getValue() instanceof JSONObject) {
				compareJSONObject((JSONObject) entry.getValue(), two.getJSONObject(entry.getKey()));
			}
		}
	}
}
