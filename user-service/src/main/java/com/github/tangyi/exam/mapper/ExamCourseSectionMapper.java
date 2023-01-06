package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExamCourseSection;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamCourseSectionMapper extends CrudMapper<ExamCourseSection> {

	List<ExamCourseSection> findSectionsByChapterId(Long id);
}