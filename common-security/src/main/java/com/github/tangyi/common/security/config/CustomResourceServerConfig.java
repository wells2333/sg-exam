package com.github.tangyi.common.security.config;

import com.github.tangyi.common.properties.FilterIgnorePropertiesConfig;
import com.github.tangyi.common.properties.SysProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 资源服务器配置
 *
 * @author tangyi
 * @date 2019-03-15 13:40
 */
@Configuration
public class CustomResourceServerConfig {

	@Bean
	public CustomWebSecurityConfigurerAdapter webSecurityConfigurerAdapter(SysProperties sysProperties,
			FilterIgnorePropertiesConfig filterIgnorePropertiesConfig) {
		return new CustomWebSecurityConfigurerAdapter(filterIgnorePropertiesConfig, sysProperties);
	}
}
