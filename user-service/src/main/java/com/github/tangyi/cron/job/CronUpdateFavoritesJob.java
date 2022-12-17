package com.github.tangyi.cron.job;

import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.exam.service.course.CourseService;
import com.github.tangyi.exam.service.exam.ExaminationService;
import com.github.tangyi.exam.service.fav.CourseFavoritesService;
import com.github.tangyi.exam.service.fav.ExamFavoritesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author tangyi
 * @date 2022/8/19 1:51 下午
 */
@Slf4j
@Component
@AllArgsConstructor
public class CronUpdateFavoritesJob {

	public static final String IS_UPDATE_FAV = EnvUtils.getValue("IS_UPDATE_FAV", "true");

	private final ExaminationService examinationService;

	private final ExamFavoritesService examFavoritesService;

	private final CourseService courseService;

	private final CourseFavoritesService courseFavoritesService;

	@Scheduled(cron = "*/30 * * * * ?")
	public void updateExamFavorites() {
		if (Boolean.parseBoolean(IS_UPDATE_FAV)) {
			StopWatch watch = StopWatchUtil.start();
			// 考试
			List<Long> examIds = examinationService.findAllIds();
			if (CollectionUtils.isNotEmpty(examIds)) {
				examFavoritesService.updateStartCount(examIds);
				examFavoritesService.updateFavorite(examIds);
				log.info("update exam favorites finished, took={}", StopWatchUtil.stop(watch));
			}
			// 课程
			List<Long> courseIds = courseService.findAllIds();
			if (CollectionUtils.isNotEmpty(courseIds)) {
				courseFavoritesService.updateStartCount(courseIds);
				courseFavoritesService.updateFavorite(courseIds);
			}
		}
	}
}
