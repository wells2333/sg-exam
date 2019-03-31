package com.github.tangyi.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableDiscoveryClient
// 扫描api包里的FeignClient
@EnableFeignClients(basePackages = {"com.github.tangyi"})
@ComponentScan(basePackages = {"com.github.tangyi"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCircuitBreaker
// 动态刷新
@RefreshScope
public class ExamServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamServiceApplication.class, args);
	}

}
