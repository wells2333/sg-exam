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
