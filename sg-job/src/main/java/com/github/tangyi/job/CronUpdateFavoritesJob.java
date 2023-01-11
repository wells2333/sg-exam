package com.github.tangyi.job;

import com.github.tangyi.api.exam.service.ICourseFavoritesService;
import com.github.tangyi.api.exam.service.ICourseService;
import com.github.tangyi.api.exam.service.IExamFavoritesService;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.StopWatchUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CronUpdateFavoritesJob {

	private static final String IS_UPDATE_FAV = EnvUtils.getValue("IS_UPDATE_FAV", "true");

	// 2分钟失效
	private static final long CRON_JOB_LOCK_EXPIRE = EnvUtils.getLong("CRON_JOB_LOCK_EXPIRE", 120);

	private static final String CRON_JOB_LOCK = "cron_job_lock";

	private final IExaminationService examinationService;

	private final IExamFavoritesService examFavoritesService;

	private final ICourseService courseService;

	private final ICourseFavoritesService courseFavoritesService;

	private final RedisTemplate<String, Long> redisTemplate;

	public CronUpdateFavoritesJob(IExaminationService examinationService, IExamFavoritesService examFavoritesService,
			ICourseService courseService, ICourseFavoritesService courseFavoritesService,
			RedisTemplate<String, Long> redisTemplate) {
		this.examinationService = examinationService;
		this.examFavoritesService = examFavoritesService;
		this.courseService = courseService;
		this.courseFavoritesService = courseFavoritesService;
		this.redisTemplate = redisTemplate;
	}

	private volatile boolean initialized;

	private volatile boolean update;

	public void init() {
		Long value = redisTemplate.opsForValue().increment(CRON_JOB_LOCK);
		if (value != null && value.equals(1L)) {
			update = true;
			if (Boolean.TRUE.equals(redisTemplate.expire(CRON_JOB_LOCK, CRON_JOB_LOCK_EXPIRE, TimeUnit.SECONDS))) {
				log.info("cron job lock expire success");
			} else {
				log.error("cron job lock expire failed");
			}
		}
		initialized = true;
	}

	@Scheduled(cron = "*/30 * * * * ?")
	public void updateExamFavorites() {
		if (!initialized) {
			init();
		}
		if (Boolean.parseBoolean(IS_UPDATE_FAV) && update) {
			try {
				doUpdateExamFavorites();
			} catch (Exception e) {
				log.error("doUpdateExamFavorites failed", e);
			}
		}
	}

	public void doUpdateExamFavorites() {
		StopWatch watch = StopWatchUtil.start();
		log.info("start to update exam fav");
		// 考试
		List<Long> examIds = examinationService.findAllIds();
		if (CollectionUtils.isNotEmpty(examIds)) {
			examFavoritesService.updateStartCount(examIds);
			examFavoritesService.updateFavorite(examIds);
			log.info("update exam fav finished, took={}", StopWatchUtil.stop(watch));
		}
		// 课程
		List<Long> courseIds = courseService.findAllIds();
		if (CollectionUtils.isNotEmpty(courseIds)) {
			courseFavoritesService.updateStartCount(courseIds);
			courseFavoritesService.updateFavorite(courseIds);
		}
		log.info("update exam fav finished");
	}
}
