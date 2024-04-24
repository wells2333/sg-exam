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

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class CopyrightTests {

	@Test
	public void testAddCopyright() throws Exception {
		URL url = CopyrightTests.class.getResource("/copyright.txt");
		Assertions.assertNotNull(url);
		Assertions.assertNotNull(url.getFile());
		File file = new File(url.getFile());
		try (InputStream in = new FileInputStream(file)) {
			byte[] bytes = FileCopyUtils.copyToByteArray(in);
			String copyright = new String(bytes, StandardCharsets.UTF_8);
			File sgExam = file.getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
			String sgExamPath = sgExam.getAbsolutePath();
			File javaFile = new File(sgExamPath + "/sg-user-service/src/main/java");
			addCopyright(javaFile, copyright);
		}
	}

	private void addCopyright(File file, String copyright) throws Exception {
		Files.walk(file.toPath()).filter(f -> f.toFile().getName().endsWith(".java")).forEach(f -> {
			try {
				String str = FileUtils.readFileToString(f.toFile(), StandardCharsets.UTF_8);
				str = copyright + "\n\n" + str;
				FileUtils.writeStringToFile(f.toFile(), str, StandardCharsets.UTF_8);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
