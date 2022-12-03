package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExamCourseKnowledgePoint;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 章节知识点Mapper
 *
 * @author tangyi
 * @date 2022-12-02
 */
@Repository
public interface ExamCourseKnowledgePointMapper extends CrudMapper<ExamCourseKnowledgePoint> {

	List<ExamCourseKnowledgePoint> findListBySectionId(Long sectionId);
}