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

package com.github.tangyi.generator.util;

import org.apache.velocity.app.Velocity;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class VelocityInitializer {

	private VelocityInitializer() {
	}

	/**
	 * 初始化 vm 方法
	 */
	public static void initVelocity() {
		Properties p = new Properties();
		try {
			// 加载 classpath 目录下的 vm 文件
			p.setProperty("resource.loader.file.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			// 定义字符集
			p.setProperty(Velocity.INPUT_ENCODING, StandardCharsets.UTF_8.name());
			// 初始化 Velocity 引擎，指定配置 Properties
			Velocity.init(p);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
