/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.user.service.sys;

import com.github.tangyi.api.user.constant.UserServiceConstant;
import com.github.tangyi.api.user.model.*;
import com.github.tangyi.api.user.service.IUserRoleService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.sys.RoleMapper;
import com.github.tangyi.user.mapper.sys.UserRoleMapper;
import com.github.tangyi.user.utils.MenuUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserRoleService extends CrudService<UserRoleMapper, UserRole> implements IUserRoleService, UserCacheName {

	private final UserRoleMapper userRoleMapper;
	private final RoleMapper roleMapper;
	private final MenuService menuService;

	public List<UserRole> getByUserId(Long userId) {
		return userRoleMapper.getByUserId(userId);
	}

	public List<UserRole> getByUserIds(List<Long> userIds) {
		return userRoleMapper.getByUserIds(userIds);
	}

	@Override
	@Cacheable(value = USER_ROLE, key = "#userAuths.tenantCode + ':' + #userAuths.identifier")
	public List<Role> getUserRoles(UserAuths userAuths) {
		List<UserRole> userRoles = userRoleMapper.getByUserId(userAuths.getUserId());
		if (CollectionUtils.isNotEmpty(userRoles)) {
			List<Role> roles = roleMapper.findListById(
					userRoles.stream().map(UserRole::getRoleId).distinct().toArray(Long[]::new));
			return CollectionUtils.isNotEmpty(roles) ? roles : Collections.emptyList();
		}
		return Collections.emptyList();
	}

	@Override
	@Cacheable(value = USER_PERMISSION, key = "#user.tenantCode + ':' + #identifier", unless = "#result == null")
	public Set<String> getUserPermissions(User user, String identifier, List<Role> roles) {
		List<Menu> menuList = Lists.newArrayList();
		// 超级管理员
		if (SysUtil.isAdmin(identifier)) {
			menuList = getAdminUserPermissions(user.getTenantCode());
		} else {
			if (CollectionUtils.isNotEmpty(roles)) {
				for (Role role : roles) {
					// 根据角色查找菜单
					List<Menu> roleMenuList = menuService.findMenuByRole(role.getRoleCode(), user.getTenantCode());
					if (CollectionUtils.isNotEmpty(roleMenuList)) {
						menuList.addAll(roleMenuList);
					}
				}
			}
		}
		return CollectionUtils.isNotEmpty(menuList) ?
				menuList.stream().filter(MenuUtil::isPermission).map(Menu::getPermission).collect(Collectors.toSet()) :
				Sets.newHashSet();
	}

	public int insertBatch(List<UserRole> userRoles) {
		return userRoleMapper.insertBatch(userRoles);
	}

	private List<Menu> getAdminUserPermissions(String tenantCode) {
		Menu condition = new Menu();
		condition.setTenantCode(tenantCode);
		condition.setType(UserServiceConstant.MENU_TYPE_PERMISSION);
		return menuService.findList(condition);
	}
}
