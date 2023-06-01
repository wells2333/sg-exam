package com.github.tangyi.common.oss.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
@ConditionalOnExpression("!'${minio}'.isEmpty()")
public class MinioConfig {

	private boolean enabled;

    private String endpoint;

    private String bucket;

    private String accessKey;

    private String secretKey;

    private int chunkSizeMb;
}
