package com.github.tangyi.api.exam.dto;

import com.github.tangyi.api.exam.model.ExamCourseSection;
import lombok.Data;

import java.util.List;

@Data
public class CourseSectionDto {

	private ExamCourseSection section;

	private String videoUrl;

	private List<KnowledgePointDto> points;
}
