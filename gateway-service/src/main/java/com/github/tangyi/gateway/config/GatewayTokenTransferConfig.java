package com.github.tangyi.gateway.config;

import com.github.tangyi.gateway.filters.TokenRequestGlobalFilter;
import com.github.tangyi.gateway.filters.TokenResponseGlobalFilter;
import com.github.tangyi.gateway.properties.GatewayTokenTransferProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 网关Token转换配置
 * 开启需要配置gateway.token-transfer.enabled=true
 *
 * @author tangyi
 * @date 2019/6/19 01:36
 */
@Configuration
@EnableConfigurationProperties(GatewayTokenTransferProperties.class)
@ConditionalOnProperty(value = "gateway.token-transfer.enabled", matchIfMissing = true)
public class GatewayTokenTransferConfig {

    @Bean
    public TokenRequestGlobalFilter tokenRequestGlobalFilter(RedisTemplate redisTemplate) {
        return new TokenRequestGlobalFilter(redisTemplate);
    }

    @Bean
    public TokenResponseGlobalFilter tokenResponseGlobalFilter(RedisTemplate redisTemplate) {
        return new TokenResponseGlobalFilter(redisTemplate);
    }
}
