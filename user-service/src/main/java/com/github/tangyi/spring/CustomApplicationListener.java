package com.github.tangyi.spring;

import com.github.tangyi.api.exam.model.ExamFavStartCount;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.exam.constants.UserFavConstant;
import com.github.tangyi.exam.service.course.CourseService;
import com.github.tangyi.exam.service.exam.ExaminationService;
import com.github.tangyi.exam.service.fav.FavStartCountService;
import com.github.tangyi.exam.service.fav.UserFavService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author tangyi
 * @date 2022/12/17 3:04 下午
 */
@Slf4j
@Component
@AllArgsConstructor
public class CustomApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	private final ExaminationService examinationService;

	private final CourseService courseService;

	private final UserFavService userFavService;

	private final FavStartCountService favStartCountService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 异步重新初始化
		new Thread(this::initStartAndFavCount).start();
	}

	public void initStartAndFavCount() {
		try {
			StopWatch start = StopWatchUtil.start();
			List<Long> examIds = examinationService.findAllIds();
			if (CollectionUtils.isNotEmpty(examIds)) {
				for (Long examId : examIds) {
					ExamFavStartCount count = favStartCountService.findByTarget(examId, UserFavConstant.FAV_TYPE_EXAM);
					setStartAndFavCount(count);
				}
			}

			List<Long> courseIds = courseService.findAllIds();
			if (CollectionUtils.isNotEmpty(courseIds)) {
				for (Long courseId : courseIds) {
					ExamFavStartCount count = favStartCountService.findByTarget(courseId,
							UserFavConstant.FAV_TYPE_COURSE);
					setStartAndFavCount(count);
				}
			}
			log.info("init start and fav count success, took: {}", StopWatchUtil.stop(start));
		} catch (Exception e) {
			log.error("init start and fav count failed", e);
		}
	}

	public void setStartAndFavCount(ExamFavStartCount count) {
		if (count != null) {
			userFavService.setStartCount(count.getTargetId(), count.getTargetType(), count.getStartCount());
			userFavService.setFavCount(count.getTargetId(), count.getTargetType(), count.getFavCount());
		}
	}
}
