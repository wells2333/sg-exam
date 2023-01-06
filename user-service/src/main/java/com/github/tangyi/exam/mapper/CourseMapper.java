package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMapper extends CrudMapper<Course> {

	List<Long> findAllIds();
}
