package com.github.tangyi.user.service;

import com.github.tangyi.api.user.module.UserRole;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.user.mapper.UserRoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tangyi
 * @date 2018/8/26 14:55
 */
@AllArgsConstructor
@Service
public class UserRoleService extends CrudService<UserRoleMapper, UserRole> {

    private final UserRoleMapper userRoleMapper;

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return List
     */
    public List<UserRole> getByUserId(Long userId) {
        return userRoleMapper.getByUserId(userId);
    }

    /**
     * 根据用户ID查询
     *
     * @param userIds 用户ID
     * @return List
     */
    public List<UserRole> getByUserIds(List<Long> userIds) {
        return userRoleMapper.getByUserIds(userIds);
    }

    /**
     * 批量插入
     *
     * @param userRoles userRoles
     * @return int
     * @author tangyi
     * @date 2019-09-03 13:13
     */
    public int insertBatch(List<UserRole> userRoles) {
        return userRoleMapper.insertBatch(userRoles);
    }
}
