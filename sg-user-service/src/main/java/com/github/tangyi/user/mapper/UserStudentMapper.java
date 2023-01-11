package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.UserStudent;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStudentMapper extends CrudMapper<UserStudent> {

    /**
     * 根据userId查询
     *
     * @param userId userId
     * @return List
     */
    List<UserStudent> getByUserId(String userId);

    /**
     * 根据studentId查询
     *
     * @param studentId studentId
     * @return UserStudent
     */
    UserStudent getByStudentId(String studentId);

    /**
     * 根据用户id删除
     *
     * @param userId userId
     * @return int
     */
    int deleteByUserId(String userId);

    /**
     * 根据学生id删除
     *
     * @param studentId studentId
     * @return int
     */
    int deleteByStudentId(String studentId);
}
