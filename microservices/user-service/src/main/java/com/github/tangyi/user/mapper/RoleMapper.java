package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.module.Role;
import com.github.tangyi.common.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色mapper
 *
 * @author tangyi
 * @date 2018/8/26 09:33
 */
@Mapper
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
