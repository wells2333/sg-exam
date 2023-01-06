package com.github.tangyi.config;

import com.github.tangyi.common.utils.EnvUtils;
import org.apache.commons.lang3.StringUtils;
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

@Configuration
public class RedisCacheConfig {

	// 超时时间：24小时
	public static final int DEFAULT_REDIS_CACHE_EXPIRE = EnvUtils.getInt("DEFAULT_REDIS_CACHE_EXPIRE", 24);

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.username}")
	private String username;

	@Value("${spring.redis.password}")
	private String redisPassword;

	@Value("${spring.redis.port:6379}")
	private int port;

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
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, port);
		if (StringUtils.isNotEmpty(username)) {
			configuration.setUsername(username);
		}
		if (StringUtils.isNotEmpty(redisPassword)) {
			configuration.setPassword(redisPassword);
		}
		return new LettuceConnectionFactory(configuration);
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
