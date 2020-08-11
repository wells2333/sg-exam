package com.github.tangyi.msc;

import com.github.tangyi.common.constant.CommonConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {CommonConstant.BASE_PACKAGE}, exclude = {DataSourceAutoConfiguration.class})
public class MscServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MscServiceApplication.class, args);
	}

}
