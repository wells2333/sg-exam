package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Course;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 课程Mapper
 *
 * @author tangyi
 * @date 2018/11/8 21:10
 */
@Repository
public interface CourseMapper extends CrudMapper<Course> {

	List<Long> findAllIds();
}
