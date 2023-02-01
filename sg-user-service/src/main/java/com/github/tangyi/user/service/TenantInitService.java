package com.github.tangyi.user.service;

import com.github.tangyi.api.user.constant.UserServiceConstant;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.api.user.model.*;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.constants.UserCacheName;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class TenantInitService {

	private final UserService userService;

	private final UserAuthsService userAuthsService;

	private final UserRoleService userRoleService;

	private final RoleService roleService;

	private final MenuService menuService;

	/**
	 * 初始化管理员账号和权限
	 */
	@Transactional
	public Role initTenant(Tenant tenant, String identifier) {
		log.info("Start to init tenant, tenantCode：{}", tenant.getTenantCode());
		StopWatch watch = StopWatchUtil.start();
		User user = initTenantAuth(tenant, identifier);
		Role role = initTenantRole(user, tenant, identifier);
		if (role == null) {
			throw new CommonException("Failed to init tenant role, tenantCode: " + tenant.getTenantCode());
		}
		initTenantMenu(tenant, role, identifier);
		log.info("Tenant has been initialed, tenantCode: {}, took: {}", tenant.getTenantCode(),
				StopWatchUtil.stop(watch));
		return role;
	}

	@Transactional
	public User initTenantAuth(Tenant tenant, String identifier) {
		User user = new User();
		user.setCommonValue(identifier, tenant.getTenantCode());
		user.setStatus(CommonConstant.STATUS_NORMAL);
		user.setName(tenant.getTenantName());
		if (userService.insert(user) > 0) {
			UserAuths userAuths = new UserAuths();
			userAuths.setCommonValue(identifier, tenant.getTenantCode());
			userAuths.setUserId(user.getId());
			// 租户管理员账号：admin_${tenantCode}
			userAuths.setIdentifier(UserServiceConstant.ADMIN_PREFIX + tenant.getTenantCode());
			userAuths.setIdentityType(IdentityType.PASSWORD.getValue());
			// 初始化默认密码
			userAuths.setCredential(userService.encodeCredential(CommonConstant.DEFAULT_PASSWORD));
			if (userAuthsService.insert(userAuths) > 0) {
				log.info("Init tenant auth success, tenantCode: {}", tenant.getTenantCode());
				return user;
			}
		}
		log.error("Failed to init tenant auth, tenantCode: {}", tenant.getTenantCode());
		return null;
	}

	/**
	 * 初始化角色
	 */
	@Transactional
	public Role initTenantRole(User user, Tenant tenant, String identifier) {
		Role role = new Role();
		String tenantCode = tenant.getTenantCode();
		role.setCommonValue(identifier, tenantCode);
		// 角色 code：role_admin_${tenantCode}
		role.setRoleCode(UserServiceConstant.ROLE_ADMIN_PREFIX + tenantCode);
		// 角色名称：单位管理员_${tenantCode}
		role.setRoleName("单位管理员_" + tenantCode);
		if (roleService.insert(role) > 0) {
			UserRole userRole = new UserRole();
			userRole.setCommonValue(identifier, tenantCode);
			userRole.setUserId(user.getId());
			userRole.setRoleId(role.getId());
			if (userRoleService.insert(userRole) > 0) {
				log.info("Init tenant role success, tenantCode: {}", tenantCode);
				return role;
			}
		}
		log.error("Failed to init tenant role, tenantCode: {}", tenantCode);
		return null;
	}

	/**
	 * 初始化菜单
	 */
	@Transactional
	@CacheEvict(value = {UserCacheName.MENU, UserCacheName.ALL_MENU, UserCacheName.USER_MENU,
			UserCacheName.ROLE_MENU}, allEntries = true)
	public void initTenantMenu(Tenant tenant, Role role, String identifier) {
		StopWatch watch = StopWatchUtil.start();
		String tenantCode = tenant.getTenantCode();
		menuService.deleteByRoleId(role.getId());
		String[] menuIds = StringUtils.split(tenant.getMenuIds(), CommonConstant.COMMA);
		if (menuIds != null && menuIds.length > 0) {
			for (String menuId : menuIds) {
				menuService.saveRoleMenu(role.getId(), Long.valueOf(menuId), identifier, tenantCode);
			}
		}
		log.info("Init tenant menus, tenantCode: {}, menu size: {}, took: {}", tenantCode,
				ArrayUtils.getLength(menuIds), StopWatchUtil.stop(watch));
	}
}
