package com.github.tangyi.exam.service.course;

import com.github.tangyi.api.exam.model.ExamCourseKnowledgePoint;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamCourseKnowledgePointMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 章节知识点Service
 * @author tangyi
 * @date 2022-12-02
 */
@Slf4j
@Service
@AllArgsConstructor
public class ExamCourseKnowledgePointService
		extends CrudService<ExamCourseKnowledgePointMapper, ExamCourseKnowledgePoint> implements ExamCacheName {

	@Override
	@Cacheable(value = ExamCacheName.KNOWLEDGE_POINT, key = "#id")
	public ExamCourseKnowledgePoint get(Long id) {
		return super.get(id);
	}

	public List<ExamCourseKnowledgePoint> findListBySectionId(Long sectionId) {
		return this.dao.findListBySectionId(sectionId);
	}

	@Override
	@Transactional
	public int insert(ExamCourseKnowledgePoint examCourseKnowledgePoint) {
		examCourseKnowledgePoint.setCommonValue();
		return super.insert(examCourseKnowledgePoint);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE_POINT, key = "#examCourseKnowledgePoint.id")
	public int update(ExamCourseKnowledgePoint examCourseKnowledgePoint) {
		examCourseKnowledgePoint.setCommonValue();
		return super.update(examCourseKnowledgePoint);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE_POINT, key = "#examCourseKnowledgePoint.id")
	public int delete(ExamCourseKnowledgePoint examCourseKnowledgePoint) {
		return super.delete(examCourseKnowledgePoint);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE_POINT, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
