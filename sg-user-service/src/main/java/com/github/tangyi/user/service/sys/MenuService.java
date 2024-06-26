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

import cn.hutool.core.collection.CollUtil;
import com.github.tangyi.api.user.constant.UserServiceConstant;
import com.github.tangyi.api.user.dto.MenuDto;
import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.api.user.model.RoleMenu;
import com.github.tangyi.api.user.service.IMenuService;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TreeUtil;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.sys.MenuMapper;
import com.github.tangyi.user.thread.ExecutorHolder;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class MenuService extends CrudService<MenuMapper, Menu> implements IMenuService, UserCacheName {

	private final MenuMapper menuMapper;
	private final RoleMenuService roleMenuService;
	private final ExecutorHolder executorHolder;

	public MenuService(MenuMapper menuMapper, RoleMenuService roleMenuService, ExecutorHolder executorHolder) {
		this.menuMapper = menuMapper;
		this.roleMenuService = roleMenuService;
		this.executorHolder = executorHolder;
	}

	public List<MenuDto> menuTree(String tenantCode) {
		Menu menu = new Menu();
		menu.setTenantCode(tenantCode);
		Stream<Menu> menuStream = findAllList(menu).stream();
		if (Optional.ofNullable(menuStream).isPresent()) {
			List<MenuDto> menuDtoList = menuStream.map(MenuDto::new).collect(Collectors.toList());
			return TreeUtil.buildTree(CollUtil.sort(menuDtoList, Comparator.comparingInt(MenuDto::getSort)),
					CommonConstant.ROOT);
		}
		return Collections.emptyList();
	}

	@Cacheable(value = USER_MENU, key = "#tenantCode + ':' + #identifier", unless = "#result == null")
	public List<MenuDto> findUserMenus(String tenantCode, String identifier) {
		return findUserMenuTree(UserServiceConstant.MENU_TYPE_MENU, identifier, tenantCode, true);
	}

	@Cacheable(value = USER_MENU_PERMISSION, key = "#tenantCode + ':' + #identifier", unless = "#result == null")
	public Set<String> findUserMenuPermissions(String tenantCode, String identifier) {
		List<MenuDto> menus = findUserMenuTree(UserServiceConstant.MENU_TYPE_PERMISSION, identifier, tenantCode, false);
		if (CollectionUtils.isNotEmpty(menus)) {
			return menus.stream().map(MenuDto::getPermission).collect(Collectors.toSet());
		}
		return null;
	}

	@Cacheable(value = TENANT_MENU, key = "#tenantCode", unless = "#result == null")
	public List<MenuDto> findDefaultTenantMenu(String tenantCode, boolean buildTree) {
		Menu condition = new Menu();
		condition.setTenantCode(tenantCode);
		return toMenuDto(findAllList(condition), null, buildTree);
	}

	@Cacheable(value = ROLE_MENU, key = "#role", unless = "#result == null")
	public List<Menu> findMenuByRole(String role, String tenantCode) {
		return menuMapper.findByRole(role, tenantCode);
	}

	@Cacheable(value = ROLE_MENU_NO_TENANT, key = "#role", unless = "#result == null")
	public List<MenuDto> findByRoleNoTeNantCode(List<String> role ) {
		return toMenuDto(menuMapper.findByRoleNoTeNantCode(role),null,true);
	}

	@Override
	@Cacheable(value = UserCacheName.ALL_MENU, key = "#menu.tenantCode", unless = "#result == null")
	public List<Menu> findAllList(Menu menu) {
		Menu condition = new Menu();
		condition.setTenantCode(SysUtil.getTenantCode());
		condition.setHideMenu(menu.getHideMenu());
		return menuMapper.findList(condition);
	}

	@Override
	@Transactional
	@CacheEvict(value = {MENU, ALL_MENU, USER_MENU, USER_MENU_PERMISSION, ROLE_MENU}, allEntries = true)
	public int insert(Menu menu) {
		return super.insert(menu);
	}

	/**
	 * 更新菜单，区分租户本身的菜单和默认租户的菜单
	 */
	@Override
	@Transactional
	@CacheEvict(value = {MENU, ALL_MENU, USER_MENU, USER_MENU_PERMISSION}, allEntries = true)
	public int update(Menu menu) {
		String tenantCode = SysUtil.getTenantCode();
		Menu source = this.get(menu.getId());
		if (tenantCode.equals(source.getTenantCode())) {
			BeanUtils.copyProperties(menu, source);
			return super.update(source);
		}
		return 0;
	}

	@Override
	@Transactional
	@CacheEvict(value = {MENU, ALL_MENU, USER_MENU, USER_MENU_PERMISSION}, allEntries = true)
	public int delete(Menu menu) {
		menu.setCommonValue();
		super.delete(menu);
		Menu parentMenu = new Menu();
		parentMenu.setParentId(menu.getId());
		parentMenu.setNewRecord(false);
		parentMenu.setCommonValue();
		parentMenu.setIsDeleted(Boolean.TRUE);
		return super.update(parentMenu);
	}

	@Transactional
	public int insertBatch(List<Menu> menus) {
		return dao.insertBatch(menus);
	}

	@Transactional
	@CacheEvict(value = {ALL_MENU, USER_MENU}, allEntries = true)
	public int deleteByTenantCode(Menu menu) {
		return this.dao.deleteByTenantCode(menu);
	}

	@Transactional
	public void saveRoleMenu(Menu menu, Role role, String identifier, String tenantCode, AtomicInteger counter) {
		if (insert(menu) > 0) {
			saveRoleMenu(role.getId(), menu.getId(), identifier, tenantCode);
			counter.incrementAndGet();
		}
	}

	@Transactional
	public void saveRoleMenu(Long roleId, Long menuId, String identifier, String tenantCode) {
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setRoleId(roleId);
		roleMenu.setMenuId(menuId);
		roleMenu.setCommonValue(identifier, tenantCode);
		roleMenuService.insert(roleMenu);
	}

	@Transactional
	public void deleteByRoleId(Long roleId) {
		roleMenuService.deleteByRoleId(roleId);
	}

	private List<Menu> findMenuByRoleList(List<Role> roleList, String tenantCode) {
		List<ListenableFuture<List<Menu>>> futures = Lists.newArrayListWithExpectedSize(roleList.size());
		for (Role role : roleList) {
			ListenableFuture<List<Menu>> future = executorHolder.getCommonExecutor().submit(() -> {
				try {
					return findMenuByRole(role.getRoleCode(), tenantCode);
				} catch (Exception e) {
					log.error("failed to findMenuByRole", e);
					return Lists.newArrayList();
				}
			});
			futures.add(future);
		}
		List<Menu> menus = Lists.newArrayList();
		try {
			List<List<Menu>> result = Futures.allAsList(futures).get(1000, TimeUnit.MILLISECONDS);
			if (CollectionUtils.isNotEmpty(result)) {
				for (List<Menu> menuList : result) {
					if (CollectionUtils.isNotEmpty(menuList)) {
						menus.addAll(menuList);
					}
				}
			}
		} catch (Exception e) {
			log.error("failed to findMenuByRoleList", e);
		}
		return menus;
	}

	private List<Menu> findAdminMenus(Byte type, String tenantCode) {
		Menu condition = new Menu();
		condition.setTenantCode(tenantCode);
		condition.setType(type);
		return findAllList(condition);
	}

	private List<Menu> findUserMenus(String tenantCode) {
		Collection<? extends GrantedAuthority> authorities = SysUtil.getAuthorities();
		if (CollectionUtils.isEmpty(authorities)) {
			return null;
		}
		List<Role> roleList = authorities.stream()
				.filter(authority -> authority.getAuthority() != null && authority.getAuthority().toLowerCase()
						.startsWith(UserServiceConstant.ROLE_PREFIX)).map(authority -> {
					Role role = new Role();
					role.setRoleCode(authority.getAuthority());
					return role;
				}).collect(Collectors.toList());
		return findMenuByRoleList(roleList, tenantCode);
	}

	private List<MenuDto> findUserMenuTree(Byte type, String identifier, String tenantCode, boolean buildTree) {
		List<Menu> userMenus = SysUtil.isAdmin(identifier) ?
				findAdminMenus(type, tenantCode) :
				findUserMenus(tenantCode);
		return toMenuDto(userMenus, type, buildTree);
	}

	private List<MenuDto> toMenuDto(List<Menu> menus, Byte type, boolean buildTree) {
		if (CollectionUtils.isEmpty(menus)) {
			return null;
		}
		List<MenuDto> list = Lists.newArrayListWithExpectedSize(menus.size());
		for (Menu menu : menus) {
			if (type != null) {
				if (type.equals(menu.getType())) {
					list.add(new MenuDto(menu));
				}
			} else {
				list.add(new MenuDto(menu));
			}
		}
		CollUtil.sort(list, Comparator.comparingInt(MenuDto::getSort));
		return buildTree ? TreeUtil.buildTree(list, CommonConstant.ROOT) : list;
	}
}
