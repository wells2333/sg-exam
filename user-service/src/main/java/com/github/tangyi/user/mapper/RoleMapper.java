package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * 角色mapper
 *
 * @author tangyi
 * @date 2018/8/26 09:33
 */
@Repository
public interface RoleMapper extends CrudMapper<Role> {

    /**
     * 根据角色code查询
     *
     * @param role role
     * @return Role
     * @author tangyi
     * @date 2019/09/21 12:08:29
     */
    Role findByRoleCode(Role role);
}
