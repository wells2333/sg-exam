package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.module.Course;
import com.github.tangyi.common.persistence.CrudMapper;
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
