package com.github.tangyi.job;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.tangyi.api.exam.service.ICourseFavoritesService;
import com.github.tangyi.api.exam.service.ICourseService;
import com.github.tangyi.api.exam.service.IExamFavoritesService;
import com.github.tangyi.api.exam.service.IExaminationService;
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
import java.util.concurrent.TimeUnit;

/**
 * <a href="https://github.com/lukas-krecan/ShedLock">ShedLock</a>
 */
@Slf4j
@Component
public class UpdateFavoritesJob {

	private static final String EXAM_IDS = "exam_ids";

	private static final String COURSE_IDS = "course_ids";

	private final IExaminationService examinationService;

	private final IExamFavoritesService examFavoritesService;

	private final ICourseService courseService;

	private final ICourseFavoritesService courseFavoritesService;

	private final Cache<String, List<Long>> cache;

	public UpdateFavoritesJob(IExaminationService examinationService, IExamFavoritesService examFavoritesService,
			ICourseService courseService, ICourseFavoritesService courseFavoritesService) {
		this.examinationService = examinationService;
		this.examFavoritesService = examFavoritesService;
		this.courseService = courseService;
		this.courseFavoritesService = courseFavoritesService;
		this.cache = Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(10).build();
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
		List<Long> examIds = getIds(EXAM_IDS, examinationService::findAllIds);
		if (CollectionUtils.isNotEmpty(examIds)) {
			examFavoritesService.updateStartCount(examIds);
			examFavoritesService.updateFavorite(examIds);
			log.info("Examination favorites has been updated successfully");
		}
		List<Long> courseIds = getIds(COURSE_IDS, courseService::findAllIds);
		if (CollectionUtils.isNotEmpty(courseIds)) {
			courseFavoritesService.updateStartCount(courseIds);
			courseFavoritesService.updateFavorite(courseIds);
			log.info("Course favorites has been updated successfully");
		}
	}

	private List<Long> getIds(String key, IdFetcher fetcher) {
		List<Long> ids = cache.getIfPresent(key);
		if (ids != null) {
			return ids;
		}
		ids = fetcher.fetch();
		if (ids == null) {
			ids = Lists.newArrayList();
		}
		cache.put(key, ids);
		return ids;
	}

	@FunctionalInterface
	public interface IdFetcher {
		List<Long> fetch();
	}
}
