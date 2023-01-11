package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExamCourseChapter;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamCourseChapterMapper extends CrudMapper<ExamCourseChapter> {

	List<ExamCourseChapter> findChaptersByCourseId(Long courseId);
}