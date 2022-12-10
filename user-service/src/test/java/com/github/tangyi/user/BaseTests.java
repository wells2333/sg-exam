package com.github.tangyi.user;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author tangyi
 * @date 2022/12/8 10:03 下午
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BaseTests {

	@ClassRule
	public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();

	@BeforeClass
	public static void setupEnv() {
		String path = "/Users/tangyi/gitee/sg-exam/config-repo";
		String configPath = String.format("file:%s/application.yml,file:%s/user-service.yml", path, path);
		environmentVariables.set("SPRING_CONFIG_LOCATION", configPath);
		environmentVariables.set("SG_DB_USER_HOST", "127.0.0.1");
		environmentVariables.set("SG_DB_USER_POST", "3306");
		environmentVariables.set("SG_DB_USER_USERNAME", "root");
		environmentVariables.set("SG_DB_USER_PASSWORD", "123456");
		environmentVariables.set("SG_DB_USER_NAME", "sg-exam-user");
	}
}
