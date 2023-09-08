package com.github.tangyi.api.exam.service;

import com.github.tangyi.api.exam.dto.KnowledgePointDto;
import com.github.tangyi.api.exam.model.ExamCourseKnowledgePoint;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;

public interface IExamCourseKnowledgePointService extends ICrudService<ExamCourseKnowledgePoint> {

	List<KnowledgePointDto> getPoints(Long sectionId);
}
