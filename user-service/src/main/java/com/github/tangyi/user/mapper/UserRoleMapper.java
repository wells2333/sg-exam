package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.UserRole;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author tangyi
 * @date 2018/8/26 14:53
 */
@Repository
public interface UserRoleMapper extends CrudMapper<UserRole> {

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return List
     */
    List<UserRole> getByUserId(Long userId);

    /**
     * 根据用户ID批量查询
     *
     * @param userIds 用户ID集合
     * @return List
     */
    List<UserRole> getByUserIds(List<Long> userIds);


    /**
     * 根据用户ID删除
     *
     * @param userId 用户ID
     * @return int
     */
    int deleteByUserId(Long userId);

    /**
     * 根据角色ID删除
     *
     * @param roleId 角色ID
     * @return int
     */
    int deleteByRoleId(Long roleId);

    /**
     * 批量插入
     *
     * @param userRoles userRoles
     * @return int
     * @author tangyi
     * @date 2019-09-03 13:14
     */
    int insertBatch(List<UserRole> userRoles);
}
