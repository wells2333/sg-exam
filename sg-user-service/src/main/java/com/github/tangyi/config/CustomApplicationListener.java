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

package com.github.tangyi.config;

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
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class CustomApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	private final ExaminationService examinationService;
	private final CourseService courseService;
	private final UserFavService userFavService;
	private final FavStartCountService favStartCountService;

	@Override
	public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
		new Thread(this::init).start();
	}

	private void init() {
		try {
			StopWatch start = StopWatchUtil.start();
			doInit();
			log.info("Init start and fav count successfully, took: {}", StopWatchUtil.stop(start));
		} catch (Exception e) {
			log.error("Failed to init start and fav count", e);
		}
	}

	private void doInit() {
		List<Long> ids = examinationService.findAllIds();
		if (CollectionUtils.isNotEmpty(ids)) {
			for (Long id : ids) {
				ExamFavStartCount count = favStartCountService.findByTarget(id, UserFavConstant.FAV_TYPE_EXAM);
				setStartAndFavCount(count);
			}
		}

		List<Long> courseIds = courseService.findAllIds();
		if (CollectionUtils.isNotEmpty(courseIds)) {
			for (Long courseId : courseIds) {
				ExamFavStartCount count = favStartCountService.findByTarget(courseId, UserFavConstant.FAV_TYPE_COURSE);
				setStartAndFavCount(count);
			}
		}
	}

	private void setStartAndFavCount(ExamFavStartCount count) {
		if (count != null) {
			userFavService.setStartCount(count.getTargetId(), count.getTargetType(), count.getStartCount());
			userFavService.setFavCount(count.getTargetId(), count.getTargetType(), count.getFavCount());
		}
	}
}
