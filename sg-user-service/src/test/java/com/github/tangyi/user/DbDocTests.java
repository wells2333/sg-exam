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
		Configuration config = Configuration.builder().version("5.0.11").description(NAME).dataSource(dataSource)
				.engineConfig(engineConfig).produceConfig(processConfig).build();
		new DocumentationExecute(config).execute();
	}
}
