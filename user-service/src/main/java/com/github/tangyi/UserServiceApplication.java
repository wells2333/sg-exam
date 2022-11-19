package com.github.tangyi;

import com.github.tangyi.prometheus.PrometheusInterceptor;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.TimeZone;

@EnableAsync
@EnableCaching
@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = {"com.github.tangyi.user.mapper", "com.github.tangyi.exam.mapper",
		"com.github.tangyi.generator.mapper", "com.github.tangyi.operation.mapper"})
public class UserServiceApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new PrometheusInterceptor()).addPathPatterns("/**");
	}

	@Bean
	public MeterRegistryCustomizer<MeterRegistry> configurer(
			@Value("${spring.application.name}") String applicationName) {
		return (registry) -> registry.config().commonTags("application", applicationName);
	}
}
