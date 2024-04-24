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

import com.github.tangyi.api.user.model.RoleMenu;
import com.github.tangyi.api.user.service.IRoleMenuService;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.sys.RoleMenuMapper;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class RoleMenuService extends CrudService<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

	private final RoleMenuMapper roleMenuMapper;

	@Transactional
	@CacheEvict(value = {UserCacheName.MENU, UserCacheName.USER_MENU}, allEntries = true)
	public int saveRoleMenus(Long roleId, String menuIds) {
		if (StringUtils.isEmpty(menuIds)) {
			return -1;
		}

		int update = 0;
		List<Long> menus = Stream.of(menuIds.split(CommonConstant.COMMA)).map(Long::parseLong)
				.collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(menus)) {
			roleMenuMapper.deleteByRoleId(roleId);
			List<RoleMenu> roleMenus = new ArrayList<>();
			for (Long menuId : menus) {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setRoleId(roleId);
				roleMenu.setMenuId(menuId);
				roleMenus.add(roleMenu);
			}
			update = roleMenuMapper.insertBatch(roleMenus);
		}
		return update;
	}

	@Transactional
	public int insertBatch(List<RoleMenu> roleMenus) {
		return roleMenuMapper.insertBatch(roleMenus);
	}

	public List<RoleMenu> getByRoleId(Long roleId) {
		return roleMenuMapper.getByRoleId(roleId);
	}

	public List<RoleMenu> getByMenuId(RoleMenu roleMenu) {
		return roleMenuMapper.getByMenuId(roleMenu);
	}

	public List<RoleMenu> getByMenuIds(List<RoleMenu> roleMenus) {
		return roleMenuMapper.getByMenuIds(roleMenus);
	}

	@Transactional
	public int deleteByRoleId(Long roleId) {
		Preconditions.checkState(roleId != null, "roleId must not null");
		return roleMenuMapper.deleteByRoleId(roleId);
	}
}
