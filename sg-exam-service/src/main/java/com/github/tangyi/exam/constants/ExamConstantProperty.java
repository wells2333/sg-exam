package com.github.tangyi.exam.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "exam.image")
public class ExamConstantProperty {

	private String CourseImageUrl;

	private String ExaminationImageUrl;

}
