package com.github.tangyi.job;

import com.github.tangyi.api.exam.service.ICourseFavoritesService;
import com.github.tangyi.api.exam.service.ICourseService;
import com.github.tangyi.api.exam.service.IExamFavoritesService;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.common.utils.StopWatchUtil;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * https://github.com/lukas-krecan/ShedLock
 */
@Slf4j
@Component
public class UpdateFavoritesJob {

	private final IExaminationService examinationService;

	private final IExamFavoritesService examFavoritesService;

	private final ICourseService courseService;

	private final ICourseFavoritesService courseFavoritesService;

	public UpdateFavoritesJob(IExaminationService examinationService, IExamFavoritesService examFavoritesService,
			ICourseService courseService, ICourseFavoritesService courseFavoritesService) {
		this.examinationService = examinationService;
		this.examFavoritesService = examFavoritesService;
		this.courseService = courseService;
		this.courseFavoritesService = courseFavoritesService;
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
		List<Long> examIds = examinationService.findAllIds();
		if (CollectionUtils.isNotEmpty(examIds)) {
			examFavoritesService.updateStartCount(examIds);
			examFavoritesService.updateFavorite(examIds);
			log.info("Examination favorites has been updated successfully");
		}
		List<Long> courseIds = courseService.findAllIds();
		if (CollectionUtils.isNotEmpty(courseIds)) {
			courseFavoritesService.updateStartCount(courseIds);
			courseFavoritesService.updateFavorite(courseIds);
			log.info("Course favorites has been updated successfully");
		}
	}
}
