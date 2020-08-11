package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.module.Student;
import com.github.tangyi.common.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生Mapper
 *
 * @author tangyi
 * @date 2019/07/09 15:27
 */
@Mapper
public interface StudentMapper extends CrudMapper<Student> {
}
