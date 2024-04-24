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

package com.github.tangyi.common.config;

import com.github.tangyi.common.utils.EnvUtils;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.Sources;
import com.wix.mysql.SqlScriptSource;
import com.wix.mysql.config.Charset;
import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.distribution.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

/**
 * profile=dev 时内嵌 MySQL 运行，不需要另外安装
 */
@Slf4j
@Profile("dev")
@Configuration
public class EmbeddedMysqlConfig {

	// 默认不启用内嵌 MySQL，可通过配置环境变量 ENABLE_EMBEDDED_MYSQL=true 启用
	private static final Boolean ENABLE_EMBEDDED_MYSQL = Boolean.valueOf(
			EnvUtils.getValue("ENABLE_EMBEDDED_MYSQL", "false"));
	public static volatile boolean INITIALIZED = false;
	private EmbeddedMysql mysql;

	@PostConstruct
	public void start() {
		if (Boolean.FALSE.equals(ENABLE_EMBEDDED_MYSQL)) {
			log.info("Embedded MySQL is disabled.");
			INITIALIZED = true;
			return;
		}

		try {
			log.info("Starting embedded MySQL...");
			MysqldConfig config = MysqldConfig.aMysqldConfig(Version.v5_7_latest)//
					.withCharset(Charset.UTF8)//
					.withPort(3306)//
					// 默认的账号密码
					.withUser("sg", "123456")//
					.withTimeZone("Asia/Shanghai")//
					.withTimeout(1, TimeUnit.MINUTES)//
					.withServerVariable("max_connect_errors", 10)//
					// 数据存储的临时目录
					.withTempDir("mysql_tmp")//
					.build();//
			mysql = EmbeddedMysql.anEmbeddedMysql(config)//
					// 初始化脚本
					.addSchema("sg-exam-user", scripts())//
					.start();//
			log.info("Embedded MySQL started successfully.");
		} catch (Exception e) {
			log.error("Failed to start embedded MySQL.", e);
		} finally {
			INITIALIZED = true;
		}

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (mysql != null) {
				mysql.stop();
				log.info("Embedded MySQL stopped successfully.");
			}
		}));
	}

	/**
	 * 获取 sg-exam/config-repo/mysql 目录下的 MySQL 初始化脚本
	 * 包括 init.sql、update.sql
	 */
	@SuppressWarnings("deprecation")
	private static List<SqlScriptSource> scripts() throws FileNotFoundException, MalformedURLException {
		String pattern = "*.sql";
		List<SqlScriptSource> results = new ArrayList<>();
		File classPath = ResourceUtils.getFile("classpath:").getAbsoluteFile();
		String projectPath = classPath.getParentFile().getParentFile().getParentFile().getParentFile().toString();
		String mysqlScriptPath = projectPath + File.separator + "config-repo" + File.separator + "mysql";
		String regexPattern = format("/%s", pattern).replace("*", ".*");
		File baseFolder = new File(mysqlScriptPath);
		File[] files = baseFolder.listFiles();
		if (files == null) {
			throw new FileNotFoundException("Files is empty, path: " + mysqlScriptPath);
		}

		for (File file : files) {
			String name = "/" + file.getName();
			if (name.matches(regexPattern)) {
				results.add(Sources.fromURL(new File(mysqlScriptPath + name).toURL()));
			}
		}
		return results;
	}
}
