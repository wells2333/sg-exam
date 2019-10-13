package com.github.tangyi.msc;

import com.github.tangyi.common.core.constant.CommonConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(scanBasePackages = {CommonConstant.BASE_PACKAGE}, exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
// 扫描api包里的FeignClient
@EnableFeignClients(basePackages = {CommonConstant.BASE_PACKAGE})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCircuitBreaker
public class MscServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MscServiceApplication.class, args);
    }

}
