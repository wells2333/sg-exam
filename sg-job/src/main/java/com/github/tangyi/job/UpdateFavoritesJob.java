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

import com.github.tangyi.api.exam.service.ICourseFavoritesService;
import com.github.tangyi.api.exam.service.ICourseService;
import com.github.tangyi.api.exam.service.IExamFavoritesService;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.common.cache.CommonCache;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <a href="https://github.com/lukas-krecan/ShedLock">ShedLock</a>
 */
@Slf4j
@Component
public class UpdateFavoritesJob {

	private final IExaminationService examinationService;

	private final IExamFavoritesService examFavoritesService;

	private final ICourseService courseService;

	private final ICourseFavoritesService courseFavoritesService;

	private final CommonCache commonCache;

	public UpdateFavoritesJob(IExaminationService examinationService, IExamFavoritesService examFavoritesService,
			ICourseService courseService, ICourseFavoritesService courseFavoritesService, CommonCache commonCache) {
		this.examinationService = examinationService;
		this.examFavoritesService = examFavoritesService;
		this.courseService = courseService;
		this.courseFavoritesService = courseFavoritesService;
		this.commonCache = commonCache;
	}

	@Scheduled(cron = "0 10 * * * ?")
	@SchedulerLock(name = "updateFavoritesJob", lockAtMostFor = "59m", lockAtLeastFor = "10m")
	public void updateFavoritesJob() {
		try {
			LockAssert.assertLocked();
			log.info("Start to update favorites");
			StopWatch watch = StopWatchUtil.start();
			doUpdateFavorites();
			log.info("Favorites has been updated successfully, took: {}", StopWatchUtil.stop(watch));
		} catch (Exception e) {
			log.error("Failed to update favorites", e);
		}
	}

	private void doUpdateFavorites() {
		List<Long> examIds = getIds(CommonCache.EXAM_IDS, examinationService::findAllIds);
		if (CollectionUtils.isNotEmpty(examIds)) {
			examFavoritesService.updateStartCount(examIds);
			examFavoritesService.updateFavorite(examIds);
			log.info("Examination favorites has been updated successfully");
		}
		List<Long> courseIds = getIds(CommonCache.COURSE_IDS, courseService::findAllIds);
		if (CollectionUtils.isNotEmpty(courseIds)) {
			courseFavoritesService.updateStartCount(courseIds);
			courseFavoritesService.updateFavorite(courseIds);
			log.info("Course favorites has been updated successfully");
		}
	}

	private List<Long> getIds(String key, IdFetcher fetcher) {
		List<Long> ids = commonCache.getCache().getIfPresent(key);
		if (ids != null) {
			return ids;
		}
		ids = fetcher.fetch();
		if (ids == null) {
			ids = Lists.newArrayList();
		}
		commonCache.getCache().put(key, ids);
		return ids;
	}

	@FunctionalInterface
	public interface IdFetcher {
		List<Long> fetch();
	}
}
