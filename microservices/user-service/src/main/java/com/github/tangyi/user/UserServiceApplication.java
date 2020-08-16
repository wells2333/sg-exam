package com.github.tangyi.user;

import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.security.annotation.EnableSgExamResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {CommonConstant.BASE_PACKAGE})
@EnableSgExamResourceServer
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
