package com.github.tangyi.user;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@ExtendWith(SystemStubsExtension.class)
public class BaseTests {

	@SystemStub
	private static EnvironmentVariables ENV;

	@BeforeAll
	public static void setupEnv() {
		File src = new File("src");
		String parentPath = src.getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath();
		String path = parentPath + File.separator + "config-repo";
		String configPath = String.format("file:%s/application.yml,file:%s/sg-user-service.yml", path, path);
		System.setProperty("SPRING_CONFIG_LOCATION", configPath);
		ENV.set("SPRING_CONFIG_LOCATION", configPath);
		try {
			List<String> lines = FileUtils.readLines(new File("src/test/resources/test.env"), StandardCharsets.UTF_8);
			for (String line : lines) {
				if (StringUtils.isNotEmpty(line)) {
					int index = line.indexOf("=");
					String name = line.substring(0, index);
					String value = line.substring(index + 1);
					System.setProperty(name, value);
					ENV.set(name, value);
					System.out.println(name + "=" + value);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
