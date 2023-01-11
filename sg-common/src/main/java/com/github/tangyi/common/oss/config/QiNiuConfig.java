package com.github.tangyi.common.oss.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "qiniu")
@ConditionalOnExpression("!'${qiniu}'.isEmpty()")
public class QiNiuConfig {

	private String accessKey;

	private String secretKey;

	private String bucket;

	/**
	 * 外部访问域名
	 */
	private String domainOfBucket;

	/**
	 * 链接超时时间，单位秒
	 */
	private Integer expire;
}
