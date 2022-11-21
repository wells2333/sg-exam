package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.model.ExamCourseSection;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamCourseSectionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 课程节Service
 * @author tangyi
 * @date 2022-11-21
 */
@Slf4j
@Service
@AllArgsConstructor
public class ExamCourseSectionService extends CrudService<ExamCourseSectionMapper, ExamCourseSection>
		implements ExamCacheName {

	@Override
	@Cacheable(value = ExamCacheName.SECTION, key = "#id")
	public ExamCourseSection get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(ExamCourseSection examCourseSection) {
		examCourseSection.setCommonValue();
		return super.insert(examCourseSection);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SECTION, key = "#examCourseSection.id")
	public int update(ExamCourseSection examCourseSection) {
		examCourseSection.setCommonValue();
		return super.update(examCourseSection);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SECTION, key = "#examCourseSection.id")
	public int delete(ExamCourseSection examCourseSection) {
		return super.delete(examCourseSection);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SECTION, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
