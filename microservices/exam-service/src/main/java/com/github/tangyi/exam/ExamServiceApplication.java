package com.github.tangyi.exam;

import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.security.annotation.EnableSgExamResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {CommonConstant.BASE_PACKAGE})
@EnableSgExamResourceServer
public class ExamServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamServiceApplication.class, args);
	}

}
