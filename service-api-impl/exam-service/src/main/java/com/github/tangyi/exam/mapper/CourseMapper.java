package com.github.tangyi.exam.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.exam.api.module.Course;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程Mapper
 *
 * @author tangyi
 * @date 2018/11/8 21:10
 */
@Mapper
public interface CourseMapper extends CrudMapper<Course> {
}
