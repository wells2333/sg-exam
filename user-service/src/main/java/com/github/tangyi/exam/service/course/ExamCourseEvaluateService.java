package com.github.tangyi.exam.service.course;

import com.github.tangyi.api.exam.model.ExamCourseEvaluate;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamCourseEvaluateMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 课程评价Service
 * @author tangyi
 * @date 2022-11-26
 */
@Slf4j
@Service
@AllArgsConstructor
public class ExamCourseEvaluateService extends CrudService<ExamCourseEvaluateMapper, ExamCourseEvaluate>
		implements ExamCacheName {

	@Override
	@Cacheable(value = ExamCacheName.EVALUATE, key = "#id")
	public ExamCourseEvaluate get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(ExamCourseEvaluate examCourseEvaluate) {
		examCourseEvaluate.setCommonValue();
		return super.insert(examCourseEvaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EVALUATE, key = "#examCourseEvaluate.id")
	public int update(ExamCourseEvaluate examCourseEvaluate) {
		examCourseEvaluate.setCommonValue();
		return super.update(examCourseEvaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EVALUATE, key = "#examCourseEvaluate.id")
	public int delete(ExamCourseEvaluate examCourseEvaluate) {
		return super.delete(examCourseEvaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EVALUATE, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
