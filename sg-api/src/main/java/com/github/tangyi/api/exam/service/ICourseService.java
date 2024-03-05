package com.github.tangyi.api.exam.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.CourseDetailDto;
import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;
import java.util.Map;

public interface ICourseService extends ICrudService<Course> {

	Long findAllCourseCount();

	List<Long> findAllIds();

	PageInfo<Course> userCourseList(Map<String, Object> params, int pageNum, int pageSize);

	PageInfo<Course> findUserCourses(Map<String, Object> params, int pageNum, int pageSize);

	List<Course> popularCourses(String findFav);

	CourseDetailDto getDetail(Long id);

	Boolean joinCourse(Long courseId, Long userId);

	CourseDetailDto getCourseAttach(Long courseId);

	void addIndex(Course course, long clickCnt, long joinCnt);

	void updateIndex(Course course);
}
