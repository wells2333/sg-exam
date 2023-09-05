package com.github.tangyi.lucene;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.exam.service.ICourseService;
import com.github.tangyi.api.exam.service.IExaminationService;
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

	private static final int MAX_WAIT_CNT = 3;

	private final Cache<String, List<Long>> cache;

	private final ICourseService courseService;

	private final IExaminationService examinationService;

	interface Initializr {
		List<Long> getIds();

		void init();
	}

	private class CourseInitializr extends IndexCrudService implements Initializr {
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

	private class ExamInitializr extends IndexCrudService implements Initializr {
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
			if (CollectionUtils.isNotEmpty(ids)) {
				for (Long id : ids) {
					Examination examination = examinationService.get(id);
					if (examination != null) {
						super.addIndex(id, DocType.EXAM, examination.getExaminationName());
					}
				}
				log.info("Add examination to index finished, size: {}", ids.size());
			}
		}
	}

	public LuceneInitializr(CommonCache commonCache, ICourseService courseService,
			IExaminationService examinationService) {
		this.cache = commonCache.getCache();
		this.courseService = courseService;
		this.examinationService = examinationService;
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
