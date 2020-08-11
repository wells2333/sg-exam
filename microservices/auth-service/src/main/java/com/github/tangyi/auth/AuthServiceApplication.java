package com.github.tangyi.auth;

import com.github.tangyi.common.constant.CommonConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {CommonConstant.BASE_PACKAGE}, exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {CommonConstant.BASE_PACKAGE})
@EnableCircuitBreaker
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
