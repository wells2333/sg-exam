package com.github.tangyi.user.mapper.sys;

import com.github.tangyi.api.user.model.UserStudent;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStudentMapper extends CrudMapper<UserStudent> {

    /**
     * 根据userId查询
     */
    List<UserStudent> getByUserId(String userId);

    /**
     * 根据 studentId 查询
     */
    UserStudent getByStudentId(String studentId);

    /**
     * 根据用户id删除
     */
    int deleteByUserId(String userId);

    /**
     * 根据学生 id 删除
     */
    int deleteByStudentId(String studentId);
}
