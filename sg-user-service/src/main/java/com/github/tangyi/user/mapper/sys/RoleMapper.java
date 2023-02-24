package com.github.tangyi.user.mapper.sys;

import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapper extends CrudMapper<Role> {

    Role findByRoleCode(Role role);

	/**
	 * 查询默认的角色
	 * @return Role
	 */
	Role findDefaultRole(String tenantCode);
}
