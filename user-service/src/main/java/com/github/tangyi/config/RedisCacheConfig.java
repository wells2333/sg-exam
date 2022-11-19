package com.github.tangyi.config;

import com.github.tangyi.common.utils.EnvUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

/**
 *
 * @author tangyi
 * @date 2022/7/2 2:16 下午
 */
@Configuration
public class RedisCacheConfig {

	// 超时时间：2小时
	public static final int DEFAULT_REDIS_CACHE_EXPIRE = EnvUtils.getInt("DEFAULT_REDIS_CACHE_EXPIRE", 2);

	@Value("${spring.redis.host}")
	private String redisHost;

	@Bean
	@Primary
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofHours(DEFAULT_REDIS_CACHE_EXPIRE))
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()));
		return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
				.cacheDefaults(redisCacheConfiguration).build();
	}

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, 6379));
	}

	@Bean
	public RedisTemplate<String, Long> longRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
		redisTemplate.setExposeConnection(true);
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	private RedisSerializer<String> keySerializer() {
		return new StringRedisSerializer();
	}

	private RedisSerializer<Object> valueSerializer() {
		return new GenericJackson2JsonRedisSerializer();
	}
}
