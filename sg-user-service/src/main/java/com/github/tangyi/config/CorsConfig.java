package com.github.tangyi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		// 支持域
		config.addAllowedOriginPattern("*");
		// 是否发送 Cookie
		config.setAllowCredentials(true);
		// 支持请求方式
		config.addAllowedMethod("*");
		// 允许的原始请求头部信息
		config.addAllowedHeader("*");
		// 暴露的头部信息
		config.addExposedHeader("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
