package com.github.tangyi.user.mapper.sys;

import com.github.tangyi.api.user.model.UserRole;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper extends CrudMapper<UserRole> {

    List<UserRole> getByUserId(Long userId);

    List<UserRole> getByUserIds(List<Long> userIds);

    int deleteByUserId(Long userId);

    int deleteByRoleId(Long roleId);

    int insertBatch(List<UserRole> userRoles);
}
