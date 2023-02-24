package com.github.tangyi.user.service.sys;

import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.api.user.service.IRoleService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SgPreCondition;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.sys.RoleMapper;
import com.github.tangyi.user.mapper.sys.RoleMenuMapper;
import com.github.tangyi.user.mapper.sys.UserRoleMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RoleService extends CrudService<RoleMapper, Role> implements IRoleService, UserCacheName {

	private final RoleMenuMapper roleMenuMapper;

	private final UserRoleMapper userRoleMapper;

	@Override
	@Cacheable(value = ROLE, key = "#role.tenantCode", unless = "#result == null")
	public List<Role> findAllList(Role role) {
		SgPreCondition.checkTenantCode(role.getTenantCode());
		return super.findAllList(role);
	}

	@Cacheable(value = ROLE, key = "#role.id", unless = "#result == null")
	public Role findByRoleCode(Role role) {
		SgPreCondition.checkTenantCode(role.getTenantCode());
		return this.dao.findByRoleCode(role);
	}

	@Override
	@Transactional
	public int insert(Role role) {
		return super.insert(role);
	}

	@Override
	@Transactional
	@CacheEvict(value = ROLE, allEntries = true)
	public int update(Role role) {
		return super.update(role);
	}

	@Override
	@Transactional
	@CacheEvict(value = ROLE, allEntries = true)
	public int delete(Role role) {
		int deleted = roleMenuMapper.deleteByRoleId(role.getId());
		log.info("Role menus has been deleted, roleId: {}, deleted: {}", role.getId(), deleted);
		deleted = userRoleMapper.deleteByRoleId(role.getId());
		log.info("User role has been deleted, roleId: {}, deleted: {}", role.getId(), deleted);
		return super.delete(role);
	}
}
