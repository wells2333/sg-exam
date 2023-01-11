package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExamCourseKnowledgePoint;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamCourseKnowledgePointMapper extends CrudMapper<ExamCourseKnowledgePoint> {

	List<ExamCourseKnowledgePoint> findListBySectionId(Long sectionId);
}