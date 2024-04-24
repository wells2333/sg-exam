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

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

public class DbDocTests extends BaseTests {

	private static final String OUTPUT = "../docs";

	private static final String NAME = "数据库设计文档";

	@Test
	public void testDbDoc() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
		hikariConfig.setJdbcUrl(
				"jdbc:mysql://" + System.getenv("SG_DB_USER_HOST") + ":3306/" + System.getenv("SG_DB_USER_NAME"));
		hikariConfig.setUsername(System.getenv("SG_DB_USER_USERNAME"));
		hikariConfig.setPassword(System.getenv("SG_DB_USER_PASSWORD"));
		hikariConfig.addDataSourceProperty("useInformationSchema", "true");
		hikariConfig.setMinimumIdle(2);
		hikariConfig.setMaximumPoolSize(5);
		DataSource dataSource = new HikariDataSource(hikariConfig);

		EngineConfig engineConfig = EngineConfig.builder().fileOutputDir(OUTPUT).fileType(EngineFileType.MD)
				.produceType(EngineTemplateType.freemarker).fileName(NAME).build();
		ProcessConfig processConfig = ProcessConfig.builder().build();
		Configuration config = Configuration.builder().version("0.0.1").description(NAME).dataSource(dataSource)
				.engineConfig(engineConfig).produceConfig(processConfig).build();
		new DocumentationExecute(config).execute();
	}
}
