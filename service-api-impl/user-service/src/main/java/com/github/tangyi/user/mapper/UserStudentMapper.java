package com.github.tangyi.user.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.user.api.module.UserStudent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户学生Mapper
 *
 * @author tangyi
 * @date 2019/07/09 15:57
 */
@Mapper
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
