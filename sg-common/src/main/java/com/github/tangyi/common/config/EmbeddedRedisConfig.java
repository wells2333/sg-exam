package com.github.tangyi.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Profile("dev")
@Configuration
public class EmbeddedRedisConfig {

	private RedisServer redis;

	@PostConstruct
	public void start() {
		try {
			log.info("Starting embedded redis...");
			this.redis = RedisServer.builder()
					.port(6379)
					.setting("bind 127.0.0.1")
					.setting("daemonize no")
					.setting("appendonly no")
					.setting("maxmemory 128M")
					.build();
			this.redis.start();
			log.info("Embedded redis started successfully");
		} catch (Exception e) {
			log.error("Failed to start embedded redis", e);
		}
	}

	@PreDestroy
	public void destroy() {
		if (this.redis != null) {
			this.redis.stop();
			log.info("Embedded redis stopped successfully");
		}
	}
}
