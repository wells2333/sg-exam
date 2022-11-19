package com.github.tangyi.cron.job;

import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.exam.service.ExaminationService;
import com.github.tangyi.exam.service.data.ExamFavoriteService;
import com.github.tangyi.exam.service.data.ExamStartCountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

	private final ExamStartCountService examStartCountService;

	private final ExamFavoriteService examFavoriteService;

	@Scheduled(cron = "*/30 * * * * ?")
	public void updateExamFavorites() {
		if (Boolean.parseBoolean(IS_UPDATE_FAV)) {
			StopWatch watch = StopWatchUtil.start();
			List<Examination> examinations = examinationService.findAllList(new Examination());
			List<Long> ids = examinations.stream().map(Examination::getId).collect(Collectors.toList());
			examStartCountService.updateStartCount(ids);
			examFavoriteService.updateFavorite(ids);
			log.info("update exam favorites finished, took={}", StopWatchUtil.stop(watch));
		}
	}
}
