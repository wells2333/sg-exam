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
