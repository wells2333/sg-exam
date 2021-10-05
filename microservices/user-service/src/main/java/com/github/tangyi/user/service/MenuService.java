package com.github.tangyi.user.service;

import cn.hutool.core.collection.CollUtil;
import com.github.tangyi.api.user.constant.MenuConstant;
import com.github.tangyi.api.user.dto.MenuDto;
import com.github.tangyi.api.user.module.Menu;
import com.github.tangyi.api.user.module.Role;
import com.github.tangyi.api.user.module.RoleMenu;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TreeUtil;
import com.github.tangyi.user.mapper.MenuMapper;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 菜单service
 *
 * @author tangyi
 * @date 2018/8/26 22:45
 */
@AllArgsConstructor
@Service
public class MenuService extends CrudService<MenuMapper, Menu> {

	private final MenuMapper menuMapper;

	private final RoleMenuService roleMenuService;

	private final SysProperties sysProperties;

	/**
	 * 返回当前用户的树形菜单集合
	 *
	 * @return List
	 * @author tangyi
	 * @date 2019-09-14 14:41
	 */
	public List<MenuDto> findUserMenu() {
		List<MenuDto> menuDtoList = new ArrayList<>();
		String tenantCode = SysUtil.getTenantCode(), identifier = SysUtil.getUser();
		List<Menu> userMenus;
		// 查询默认租户的菜单
		Menu condition = new Menu();
		condition.setTenantCode(CommonConstant.DEFAULT_TENANT_CODE);
		condition.setApplicationCode(SysUtil.getSysCode());
		condition.setType(MenuConstant.MENU_TYPE_MENU);
		List<Menu> defaultMenus = findAllList(condition);

		// 超级管理员
		if (identifier.equals(sysProperties.getAdminUser())) {
			// 获取租户的菜单和默认租户的菜单，最后组装数据，租户的菜单优先
			if (CommonConstant.DEFAULT_TENANT_CODE.equals(tenantCode)) {
				userMenus = defaultMenus;
			} else {
				// 获取角色的菜单
				condition.setTenantCode(tenantCode);
				condition.setApplicationCode(SysUtil.getSysCode());
				condition.setType(MenuConstant.MENU_TYPE_MENU);
				List<Menu> tenantMenus = findAllList(condition);
				// 组装数据
				userMenus = mergeMenu(defaultMenus, tenantMenus);
			}
		} else {
			//			List<Role> roleList = SysUtil.getCurrentAuthentication().getAuthorities().stream()
			//					// 按角色过滤
			//					.filter(authority -> authority.getAuthority() != null && authority.getAuthority()
			//							.startsWith("ROLE_")).map(authority -> {
			//						Role role = new Role();
			//						role.setRoleCode(authority.getAuthority());
			//						return role;
			//					}).collect(Collectors.toList());
			List<Role> roleList = Collections.emptyList();
			// 根据角色code批量查找菜单
			List<Menu> tenantMenus = finMenuByRoleList(roleList, tenantCode);
			// 组装数据
			userMenus = mergeMenu(getTenantMenus(defaultMenus), tenantMenus);
		}
		if (CollectionUtils.isNotEmpty(userMenus)) {
			userMenus.stream()
					// 菜单类型
					.filter(menu -> MenuConstant.MENU_TYPE_MENU.equals(menu.getType()))
					// dto封装
					.map(MenuDto::new)
					// 去重
					.distinct().forEach(menuDtoList::add);
			// 排序、构建树形关系
			return TreeUtil.buildTree(CollUtil.sort(menuDtoList, Comparator.comparingInt(MenuDto::getSort)),
					CommonConstant.ROOT);
		}
		return Lists.newArrayList();
	}

	/**
	 * 根据角色查找菜单
	 *
	 * @param role       角色标识
	 * @param tenantCode 租户标识
	 * @return List
	 * @author tangyi
	 * @date 2018/8/27 16:00
	 */
	@Cacheable(value = "menu#" + CommonConstant.CACHE_EXPIRE, key = "#role")
	public List<Menu> findMenuByRole(String role, String tenantCode) {
		List<Menu> menus = new ArrayList<>();
		// 返回默认租户的角色菜单
		if (!tenantCode.equals(CommonConstant.DEFAULT_TENANT_CODE))
			menus = menuMapper.findByRole(role, CommonConstant.DEFAULT_TENANT_CODE);
		List<Menu> tenantMenus = menuMapper.findByRole(role, tenantCode);
		if (CollectionUtils.isNotEmpty(tenantMenus))
			menus.addAll(tenantMenus);
		return menus;
	}

	/**
	 * 批量查询菜单
	 *
	 * @param roleList   roleList
	 * @param tenantCode tenantCode
	 * @return List
	 * @author tangyi
	 * @date 2019/07/03 23:50:35
	 */
	private List<Menu> finMenuByRoleList(List<Role> roleList, String tenantCode) {
		List<Menu> menus = Lists.newArrayList();
		for (Role role : roleList) {
			List<Menu> roleMenus = this.findMenuByRole(role.getRoleCode(), tenantCode);
			if (CollectionUtils.isNotEmpty(roleMenus))
				menus.addAll(roleMenus);
		}
		return menus;
	}

	/**
	 * 查询全部菜单，包括租户本身的菜单和默认租户的菜单
	 *
	 * @param menu menu
	 * @return List
	 * @author tangyi
	 * @date 2019/04/10 17:58
	 */
	@Override
	public List<Menu> findAllList(Menu menu) {
		List<Menu> menus = new ArrayList<>();
		if (!menu.getTenantCode().equals(CommonConstant.DEFAULT_TENANT_CODE)) {
			Menu defaultMenu = new Menu();
			defaultMenu.setApplicationCode(SysUtil.getSysCode());
			defaultMenu.setTenantCode(CommonConstant.DEFAULT_TENANT_CODE);
			menus = menuMapper.findAllList(defaultMenu);
		}
		List<Menu> tenantMenus = menuMapper.findAllList(menu);
		if (CollectionUtils.isNotEmpty(tenantMenus))
			menus = mergeMenu(menus, tenantMenus);
		return menus;
	}

	/**
	 * 新增菜单
	 *
	 * @param menu menu
	 * @return int
	 * @author tangyi
	 * @date 2018/10/28 15:56
	 */
	@Transactional
	@Override
	@CacheEvict(value = {"menu", "user"}, allEntries = true)
	public int insert(Menu menu) {
		return super.insert(menu);
	}

	/**
	 * 更新菜单，区分租户本身的菜单和默认租户的菜单
	 *
	 * @param menu menu
	 * @return int
	 * @author tangyi
	 * @date 2018/10/30 20:19
	 */
	@Transactional
	@Override
	@CacheEvict(value = {"menu", "user"}, allEntries = true)
	public int update(Menu menu) {
		String userCode = SysUtil.getUser();
		String sysCode = SysUtil.getSysCode();
		String tenantCode = SysUtil.getTenantCode();
		Menu source = this.get(menu);
		// 默认租户的用户更新菜单或更新本租户的菜单，直接更新
		if (tenantCode.equals(CommonConstant.DEFAULT_TENANT_CODE) || tenantCode.equals(source.getTenantCode())) {
			BeanUtils.copyProperties(menu, source);
			return super.update(source);
		} else {
			// 其它租户更新默认租户的菜单，copy一份原始菜单的数据
			Long originalId = menu.getId();
			String originalTenantCode = menu.getTenantCode();
			// 重新初始化ID
			menu.setId(null);
			menu.setCommonValue(userCode, sysCode, tenantCode);
			this.insert(menu);
			// copy子菜单
			Long newMenuId = menu.getId();
			Menu condition = new Menu();
			condition.setParentId(originalId);
			condition.setTenantCode(originalTenantCode);
			// 查询子菜单
			List<Menu> childrenMenus = findList(condition);
			// 子菜单ID列表
			List<Long> childrenMenuIds = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(childrenMenus)) {
				childrenMenus.forEach(children -> {
					childrenMenuIds.add(children.getId());
					// 重新初始化ID
					children.setId(null);
					children.setCommonValue(userCode, sysCode, tenantCode);
					// 重新绑定父菜单
					children.setParentId(newMenuId);
				});
				// 批量插入
				insertBatch(childrenMenus);
			}
			// 更新权限数据roleMenu
			updateRoleMenu(originalId, childrenMenuIds, userCode, sysCode, tenantCode);
		}
		return super.update(menu);
	}

	/**
	 * 删除菜单
	 *
	 * @param menu menu
	 * @return int
	 * @author tangyi
	 * @date 2018/8/27 16:22
	 */
	@Override
	@Transactional
	@CacheEvict(value = {"menu", "user"}, allEntries = true)
	public int delete(Menu menu) {
		menu.setCommonValue();
		// 删除当前菜单
		super.delete(menu);
		// 删除父节点为当前节点的菜单
		Menu parentMenu = new Menu();
		parentMenu.setParentId(menu.getId());
		parentMenu.setNewRecord(false);
		parentMenu.setCommonValue();
		parentMenu.setDelFlag(CommonConstant.DEL_FLAG_DEL);
		return super.update(parentMenu);
	}

	/**
	 * 批量插入
	 *
	 * @param menus menus
	 * @return int
	 * @author tangyi
	 * @date 2019-09-03 12:19
	 */
	@Transactional
	public int insertBatch(List<Menu> menus) {
		return dao.insertBatch(menus);
	}

	/**
	 * 合并默认租户和租户的菜单，租户菜单优先
	 *
	 * @param defaultMenus defaultMenus
	 * @param tenantMenus  tenantMenus
	 * @return List
	 * @author tangyi
	 * @date 2019-09-14 14:45
	 */
	private List<Menu> mergeMenu(List<Menu> defaultMenus, List<Menu> tenantMenus) {
		if (CollectionUtils.isEmpty(tenantMenus))
			return defaultMenus;
		List<Menu> userMenus = new ArrayList<>();
		// 默认菜单
		defaultMenus.forEach(defaultMenu -> {
			Optional<Menu> menu = tenantMenus.stream()
					.filter(tenantMenu -> tenantMenu.getName().equals(defaultMenu.getName())).findFirst();
			if (menu.isPresent()) {
				userMenus.add(menu.get());
			} else {
				userMenus.add(defaultMenu);
			}
		});
		// 租户菜单
		tenantMenus.forEach(tenantMenu -> {
			Optional<Menu> exist = userMenus.stream()
					.filter(userMenu -> userMenu.getName().equals(tenantMenu.getName()) && userMenu.getParentId()
							.equals(tenantMenu.getParentId())).findFirst();
			if (!exist.isPresent()) {
				userMenus.add(tenantMenu);
			}
		});
		return userMenus;
	}

	/**
	 * 更新权限数据
	 *
	 * @param menuId          menuId
	 * @param childrenMenuIds childrenMenuIds
	 * @param userCode        userCode
	 * @param sysCode         sysCode
	 * @param tenantCode      tenantCode
	 * @return
	 * @author tangyi
	 * @date 2019-09-14 15:57
	 */
	@Transactional
	public void updateRoleMenu(Long menuId, List<Long> childrenMenuIds, String userCode, String sysCode,
			String tenantCode) {
		List<RoleMenu> condition = new ArrayList<>();
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setMenuId(menuId);
		// 主菜单
		condition.add(roleMenu);
		// 子菜单
		if (CollectionUtils.isNotEmpty(childrenMenuIds)) {
			childrenMenuIds.forEach(childrenMenuId -> {
				RoleMenu childRoleMenu = new RoleMenu();
				childRoleMenu.setMenuId(childrenMenuId);
				condition.add(childRoleMenu);
			});
		}
		// 查询
		List<RoleMenu> roleMenus = roleMenuService.getByMenuIds(condition);
		if (CollectionUtils.isNotEmpty(roleMenus)) {
			roleMenus.forEach(tempRoleMenu -> {
				// 重新初始化ID
				tempRoleMenu.setId(null);
				tempRoleMenu.setCommonValue(userCode, sysCode, tenantCode);
			});
			// 批量插入
			roleMenuService.insertBatch(roleMenus);
		}
	}

	/**
	 * 根据租户code删除
	 *
	 * @param menu menu
	 * @return int
	 */
	@Transactional
	public int deleteByTenantCode(Menu menu) {
		return this.dao.deleteByTenantCode(menu);
	}

	/**
	 * 获取租户权限的菜单
	 * @param defaultMenus defaultMenus
	 * @return List
	 */
	private List<Menu> getTenantMenus(List<Menu> defaultMenus) {
		List<Menu> tenantMenus = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(defaultMenus)) {
			defaultMenus.forEach(menu -> {
				String permission = menu.getPermission();
				// 过滤客户端管理、路由管理、系统监控菜单
				if (!permission.equals(MenuConstant.MENU_CLIENT) && !permission.equals(MenuConstant.MENU_ROUTE)
						&& !permission.equals(MenuConstant.MENU_TENANT) && !permission
						.equals(MenuConstant.MENU_MONITOR)) {
					tenantMenus.add(menu);
				}
			});
		}
		return tenantMenus;
	}

	/**
	 * 返回树形菜单集合
	 *
	 * @return 树形菜单集合
	 */
	public List<MenuDto> menus() {
		// 查询所有菜单
		Menu condition = new Menu();
		condition.setApplicationCode(SysUtil.getSysCode());
		condition.setTenantCode(SysUtil.getTenantCode());
		Stream<Menu> menuStream = findAllList(condition).stream();
		if (Optional.ofNullable(menuStream).isPresent()) {
			// 转成MenuDto
			List<MenuDto> menuDtoList = menuStream.map(MenuDto::new).collect(Collectors.toList());
			// 排序、构建树形关系
			return TreeUtil.buildTree(CollUtil.sort(menuDtoList, Comparator.comparingInt(MenuDto::getSort)),
					CommonConstant.ROOT);
		}
		return new ArrayList<>();
	}
}
