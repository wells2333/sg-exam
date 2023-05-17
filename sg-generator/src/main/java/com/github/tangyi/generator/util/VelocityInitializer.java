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
