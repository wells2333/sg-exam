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

package com.github.tangyi.job;

import com.github.tangyi.common.utils.StopWatchUtil;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <a href="https://github.com/lukas-krecan/ShedLock">ShedLock</a>
 */
@Slf4j
@Component
public class UpdateFavoritesJob {

	private static final String UPDATE_FAVORITES_CRON_EXPR = "0 0/1 * * * ?";

	@Scheduled(cron = UPDATE_FAVORITES_CRON_EXPR)
	@SchedulerLock(name = "updateFavoritesJob", lockAtMostFor = "59m", lockAtLeastFor = "10m")
	public void updateFavoritesJob() {
		try {
			LockAssert.assertLocked();
			log.info("Start to update favorites");
			StopWatch watch = StopWatchUtil.start();
			// TODO 将收藏数量同步到 Redis
			log.info("Favorites has been updated successfully, took: {}", StopWatchUtil.stop(watch));
		} catch (Exception e) {
			log.error("Failed to update favorites", e);
		}
	}
}
