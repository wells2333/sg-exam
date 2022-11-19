package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.Student;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * 学生Mapper
 *
 * @author tangyi
 * @date 2019/07/09 15:27
 */
@Repository
public interface StudentMapper extends CrudMapper<Student> {
}
