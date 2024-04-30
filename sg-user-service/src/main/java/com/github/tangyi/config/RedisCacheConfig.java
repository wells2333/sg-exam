/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.config;

import com.github.tangyi.common.utils.EnvUtils;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PreDestroy;
import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

	// 超时时间：24 小时
	public static final int DEFAULT_REDIS_CACHE_EXPIRE = EnvUtils.getInt("DEFAULT_REDIS_CACHE_EXPIRE", 24);

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.username}")
	private String username;

	@Value("${spring.redis.password}")
	private String redisPassword;

	@Value("${spring.redis.port:6379}")
	private int port;

	@Value("${spring.redis.timeout:60}")
	private int timeOutSecond;

	private LettuceConnectionFactory factory;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, port);
		if (StringUtils.isNotEmpty(username)) {
			configuration.setUsername(username);
		}
		if (StringUtils.isNotEmpty(redisPassword)) {
			configuration.setPassword(redisPassword);
		}
		SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(timeOutSecond)).build();
		ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

		LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
				.clientOptions(clientOptions).build();
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration,
				lettuceClientConfiguration);
		this.factory = connectionFactory;
		return connectionFactory;
	}

	@Bean
	public CacheManager cacheManager(LettuceConnectionFactory factory) {
		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofHours(DEFAULT_REDIS_CACHE_EXPIRE)).serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
						new GenericJackson2JsonRedisSerializer()));
		return RedisCacheManager.builder(factory).cacheDefaults(configuration).build();
	}

	@Bean
	public RedisTemplate<String, Long> longRedisTemplate(LettuceConnectionFactory factory) {
		RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setConnectionFactory(factory);
		return redisTemplate;
	}

	@PreDestroy
	public void cleanup() {
		if (this.factory != null) {
			this.factory.destroy();
		}
	}
}
