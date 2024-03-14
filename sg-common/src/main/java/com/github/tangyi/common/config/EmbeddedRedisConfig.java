package com.github.tangyi.common.config;

import com.github.tangyi.common.utils.EnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;

@Slf4j
@Profile("dev")
@Configuration
public class EmbeddedRedisConfig {

	// 默认启用内嵌 Redis，可通过配置环境变量 ENABLE_EMBEDDED_REDIS=false 禁用
	private static final Boolean ENABLE_EMBEDDED_REDIS = Boolean.valueOf(
			EnvUtils.getValue("ENABLE_EMBEDDED_REDIS", "true"));

	private RedisServer redis;

	@PostConstruct
	public void start() {
		if (!ENABLE_EMBEDDED_REDIS) {
			return;
		}

		try {
			log.info("Starting embedded redis...");
			this.redis = RedisServer.builder()    //
					.port(6379)    //
					.setting("bind 127.0.0.1")    //
					.setting("daemonize no")    //
					.setting("appendonly no")    //
					.setting("maxmemory 128M")    //
					.build();    //
			this.redis.start();
			log.info("Embedded redis started successfully.");
		} catch (Exception e) {
			log.error("Failed to start embedded redis.", e);
		}

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (redis != null) {
				redis.stop();
				log.info("Embedded redis stopped successfully.");
			}
		}));
	}
}
