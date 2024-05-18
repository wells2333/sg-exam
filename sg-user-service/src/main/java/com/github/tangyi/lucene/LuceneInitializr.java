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

package com.github.tangyi.lucene;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.api.exam.model.ExamCourseMember;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.exam.service.ICourseService;
import com.github.tangyi.api.exam.service.IExamCourseMemberService;
import com.github.tangyi.api.exam.service.IExamPermissionService;
import com.github.tangyi.api.exam.service.IExaminationService;
import com.github.tangyi.common.cache.CommonCache;
import com.github.tangyi.common.config.EmbeddedMysqlConfig;
import com.github.tangyi.common.lucene.LuceneIndexManager;
import com.github.tangyi.common.service.IndexCrudService;
import com.github.tangyi.constants.ExamConstant;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public final class LuceneInitializr {

	private static final int MAX_WAIT_CNT = 3;

	private final Cache<String, List<Long>> cache;
	private final ICourseService courseService;
	private final IExaminationService examinationService;
	private final IExamCourseMemberService courseMemberService;
	private final IExamPermissionService examinationMemberService;

	interface Initializr {

		List<Long> getIds();

		void init();
	}

	private final class CourseInitializr extends IndexCrudService implements Initializr {
		@Override
		public List<Long> getIds() {
			List<Long> ids = waitGetIdsFromCache(cache.getIfPresent(CommonCache.COURSE_IDS), CommonCache.COURSE_IDS);
			if (ids == null) {
				ids = courseService.findAllIds();
			}
			return ids;
		}

		@Override
		public void init() {
			List<Long> ids = getIds();
			if (CollectionUtils.isEmpty(ids)) {
				return;
			}

			ExamCourseMember member = new ExamCourseMember();
			for (Long id : ids) {
				Course course = courseService.get(id);
				if (course != null) {
					member.setCourseId(course.getId());
					Integer memberCnt = courseMemberService.findMemberCountByCourseId(member);
					long joinCnt = memberCnt == null ? 0 : memberCnt;
					courseService.addIndex(course, joinCnt, joinCnt);
				}
			}
			log.info("Add course to index finished, size: {}", ids.size());
		}
	}

	private final class ExamInitializr extends IndexCrudService implements Initializr {
		@Override
		public List<Long> getIds() {
			List<Long> ids = waitGetIdsFromCache(cache.getIfPresent(CommonCache.EXAM_IDS), CommonCache.EXAM_IDS);
			if (ids == null) {
				ids = examinationService.findAllIds();
			}
			return ids;
		}

		@Override
		public void init() {
			List<Long> ids = getIds();
			if (CollectionUtils.isEmpty(ids)) {
				return;
			}

			for (Long id : ids) {
				Examination examination = examinationService.get(id);
				if (examination != null) {
					Integer memberCnt = examinationMemberService.findCount(ExamConstant.PERMISSION_TYPE_EXAM,
							examination.getId());
					long joinCnt = memberCnt == null ? 0 : memberCnt;
					examinationService.addIndex(examination, joinCnt, joinCnt);
				}
			}
			log.info("Add examination to index finished, size: {}", ids.size());
		}
	}

	public LuceneInitializr(CommonCache commonCache, ICourseService courseService,
			IExaminationService examinationService, IExamCourseMemberService courseMemberService,
			IExamPermissionService examinationMemberService) {
		this.cache = commonCache.getCache();
		this.courseService = courseService;
		this.examinationService = examinationService;
		this.courseMemberService = courseMemberService;
		this.examinationMemberService = examinationMemberService;
		List<Initializr> initializers = Lists.newArrayList(new CourseInitializr(), new ExamInitializr());
		new Thread(() -> {
			while (!EmbeddedMysqlConfig.INITIALIZED) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					log.error("Wait MySQL init failed.", e);
					throw new RuntimeException(e);
				}
			}

			log.info("Start to execute lucene initializr.");
			for (Initializr initializr : initializers) {
				initializr.init();
			}
			LuceneIndexManager.getInstance().init();
			log.info("LuceneInitializr finished.");
		}).start();
		log.info("LuceneInitializr started.");
	}

	private List<Long> waitGetIdsFromCache(List<Long> ids, String cacheKey) {
		int waitCnt = 0;
		while (ids == null && ++waitCnt < MAX_WAIT_CNT) {
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				// Ignore.
			}

			ids = cache.getIfPresent(cacheKey);
		}
		return ids;
	}
}
