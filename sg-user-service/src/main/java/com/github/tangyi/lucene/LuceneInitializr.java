package com.github.tangyi.lucene;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.api.exam.service.ICourseService;
import com.github.tangyi.common.cache.CommonCache;
import com.github.tangyi.common.lucene.DocType;
import com.github.tangyi.common.service.IndexCrudService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LuceneInitializr {

	private static final int MAX_WAIT_CNT = 2;

	private final Cache<String, List<Long>> cache;

	private final ICourseService courseService;

	interface Initializr {
		List<Long> getIds();

		void init();
	}

	class CourseInitializr extends IndexCrudService implements Initializr {
		@Override
		public List<Long> getIds() {
			List<Long> ids = cache.getIfPresent(CommonCache.COURSE_IDS);
			int waitCnt = 0;
			while (ids == null && ++waitCnt < MAX_WAIT_CNT) {
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					// Ignore.
				}
				ids = cache.getIfPresent(CommonCache.COURSE_IDS);
			}
			if (ids == null) {
				ids = courseService.findAllIds();
			}
			return ids;
		}

		@Override
		public void init() {
			List<Long> ids = getIds();
			if (CollectionUtils.isNotEmpty(ids)) {
				for (Long id : ids) {
					Course course = courseService.get(id);
					if (course != null) {
						super.addIndex(id, DocType.COURSE, course.getCourseName(), course.getCourseDescription());
					}
				}
				log.info("Add course to index finished, size: {}", ids.size());
			}
		}
	}

	static class ExamInitializr extends IndexCrudService implements Initializr {
		@Override
		public List<Long> getIds() {
			return Lists.newArrayList();
		}

		@Override
		public void init() {

		}
	}

	public LuceneInitializr(CommonCache commonCache, ICourseService courseService) {
		this.cache = commonCache.getCache();
		this.courseService = courseService;
		List<Initializr> initializers = Lists.newArrayList(new CourseInitializr(), new ExamInitializr());
		new Thread(() -> {
			log.info("Start to execute lucene initializr.");
			for (Initializr initializr : initializers) {
				initializr.init();
			}
			log.info("LuceneInitializr finished.");
		}).start();
		log.info("LuceneInitializr started.");
	}
}
