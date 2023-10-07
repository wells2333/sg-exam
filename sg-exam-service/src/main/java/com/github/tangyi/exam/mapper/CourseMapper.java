package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CourseMapper extends CrudMapper<Course> {

	Long findAllCourseCount();

	List<Long> findIdsOrderByIdAsc(@Param("minId") long minId, @Param("pageSize") int pageSize, @Param("params") Map<String, Object> params);

	List<Course> findUserCourses(@Param("params") Map<String, Object> params);

}
