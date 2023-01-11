package com.github.tangyi.user.service;

import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.api.user.service.IRoleService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.RoleMapper;
import com.github.tangyi.user.mapper.RoleMenuMapper;
import com.github.tangyi.user.mapper.UserRoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class RoleService extends CrudService<RoleMapper, Role> implements IRoleService {

	private final RoleMenuMapper roleMenuMapper;

	private final UserRoleMapper userRoleMapper;

	@Override
	@Cacheable(value = UserCacheName.ROLE, key = "#role.tenantCode")
	public List<Role> findAllList(Role role) {
		return super.findAllList(role);
	}

	@Cacheable(value = UserCacheName.ROLE, key = "#role.id")
	public Role findByRoleCode(Role role) {
		return this.dao.findByRoleCode(role);
	}

	@Override
	@Transactional
	public int insert(Role role) {
		return super.insert(role);
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.ROLE, key = "#role.id")
	public int update(Role role) {
		return super.update(role);
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.ROLE, key = "#role.id")
	public int delete(Role role) {
		// 删除角色菜单关系
		roleMenuMapper.deleteByRoleId(role.getId());
		// 删除用户角色关系
		userRoleMapper.deleteByRoleId(role.getId());
		return super.delete(role);
	}
}
