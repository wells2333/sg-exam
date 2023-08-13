package com.github.tangyi.common.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CommonCache {

	public static final String EXAM_IDS = "exam_ids";

	public static final String COURSE_IDS = "course_ids";

	private final Cache<String, List<Long>> cache;

	public CommonCache() {
		this.cache = Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(10).build();
	}

	public Cache<String, List<Long>> getCache() {
		return cache;
	}
}
