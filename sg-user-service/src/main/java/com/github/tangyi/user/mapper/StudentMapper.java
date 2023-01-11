package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.Student;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMapper extends CrudMapper<Student> {
}
