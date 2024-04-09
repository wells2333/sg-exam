package com.github.tangyi.config;

import com.github.tangyi.common.utils.EnvUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * 序列化数据给前端的配置
 * 主要配置时区，将 Date 对象序列化成字符串时的时区
 */
@Configuration
public class JacksonConfig {

	// 默认时区为 GMT+8
	private static final String DEFAULT_TIMEZONE = EnvUtils.getValue("user.timezone", "GMT+8");

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return builder -> {
			builder.timeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
		};
	}
}
