package com.github.tangyi.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;

/**
 * 内嵌Redis，用于dev环境
 * @author tangyi
 * @date 2020/8/4 11:19 下午
 */
@Configuration
@Profile("dev")
@Slf4j
public class EmbeddedRedisConfig {

	@PostConstruct
	public void startRedis() {
		try {
			log.info("start redis server");
			RedisServer redisServer = RedisServer.builder()
					.port(6379)
					.setting("bind 127.0.0.1")
					.setting("daemonize no")
					.setting("appendonly no")
					.setting("maxmemory 128M")
					.setting("maxheap 512M")
					.build();
			redisServer.start();
			log.info("start redis success");
		} catch (Exception e) {
			log.error("start redis server error: {}", e.getMessage());
		}
	}
}
