package com.github.tangyi.user;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BaseTests {

	@ClassRule
	public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();

	@BeforeClass
	public static void setupEnv() throws Exception {
		File src = new File("src");
		String parentPath = src.getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath();
		String path = parentPath + File.separator + "config-repo";
		String configPath = String.format("file:%s/application.yml,file:%s/sg-user-service.yml", path, path);
		environmentVariables.set("SPRING_CONFIG_LOCATION", configPath);
		List<String> lines = FileUtils.readLines(new File("src/test/resources/test.env"), StandardCharsets.UTF_8);
		for (String line : lines) {
			if (StringUtils.isNotEmpty(line)) {
				int index = line.indexOf("=");
				String name = line.substring(0, index);
				String value = line.substring(index + 1);
				System.setProperty(name, value);
				environmentVariables.set(name, value);
				System.out.println(name + "=" + value);
			}
		}
	}
}
