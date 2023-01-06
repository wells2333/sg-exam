package com.github.tangyi.exam.service.course;

import com.github.tangyi.api.exam.dto.CourseSectionDto;
import com.github.tangyi.api.exam.model.ExamCourseSection;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamCourseSectionMapper;
import com.github.tangyi.exam.service.media.ExamMediaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程节Service
 */
@Slf4j
@Service
@AllArgsConstructor
public class ExamCourseSectionService extends CrudService<ExamCourseSectionMapper, ExamCourseSection>
		implements ExamCacheName {

	private final ExamMediaService mediaService;

	@Override
	@Cacheable(value = ExamCacheName.SECTION, key = "#id")
	public ExamCourseSection get(Long id) {
		return super.get(id);
	}

	public CourseSectionDto watchSection(Long id) {
		CourseSectionDto dto = new CourseSectionDto();
		ExamCourseSection section = this.get(id);
		if (section != null && section.getVideoId() != null) {
			dto.setVideoUrl(mediaService.videoUrl(section.getVideoId()));
		}
		dto.setSection(section);
		return dto;
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

	public List<ExamCourseSection> findSectionsByChapterId(Long id) {
		return this.dao.findSectionsByChapterId(id);
	}
}
