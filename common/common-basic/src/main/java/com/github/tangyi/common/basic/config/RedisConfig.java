package com.github.tangyi.common.basic.config;

import com.github.tangyi.common.basic.cache.CustomRedisCacheWriter;
import com.github.tangyi.common.basic.cache.MultitenantCacheManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * redis配置
 *
 * @author tangyi
 * @date 2019/3/16 20:40
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 存储对象需要配置序列化机制
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 多租户cacheManager
     *
     * @return RedisCacheManager
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, CacheManagerCustomizers customizerInvoker) {
        RedisCacheWriter redisCacheWriter = new CustomRedisCacheWriter(connectionFactory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        Map<String, RedisCacheConfiguration> initialCacheConfigurations = new LinkedHashMap<>();
        // 多租户cacheManager
        RedisCacheManager cacheManager = new MultitenantCacheManager(redisCacheWriter, redisCacheConfiguration, initialCacheConfigurations, true);
        cacheManager.setTransactionAware(false);
        return customizerInvoker.customize(cacheManager);
    }

    @Bean
    public CacheManagerCustomizers cacheManagerCustomizers(
            ObjectProvider<List<CacheManagerCustomizer<?>>> customizers) {
        return new CacheManagerCustomizers(customizers.getIfAvailable());
    }
}
