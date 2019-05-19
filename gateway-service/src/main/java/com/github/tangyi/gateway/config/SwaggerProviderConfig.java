package com.github.tangyi.gateway.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供swagger文档的服务
 * @author tangyi
 * @date 2019/3/27 16:24
 */
@Data
@Configuration
@RefreshScope
@ConditionalOnExpression("!'${swagger}'.isEmpty()")
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProviderConfig {

    private List<String> providers = new ArrayList<>();

}
