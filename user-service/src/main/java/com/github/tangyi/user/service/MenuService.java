package com.github.tangyi.user.service;

import cn.hutool.core.collection.CollUtil;
import com.github.tangyi.api.user.constant.MenuConstant;
import com.github.tangyi.api.user.dto.MenuDto;
import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.api.user.model.RoleMenu;
import com.github.tangyi.common.base.TreeEntity;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TreeUtil;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.MenuMapper;
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

/**
 * 菜单service
 *
 * @author tangyi
 * @date 2018/8/26 22:45
 */
@Slf4j
@Service
public class MenuService extends CrudService<MenuMapper, Menu> {

	private final MenuMapper menuMapper;

	private final RoleMenuService roleMenuService;

	private final CommonExecutorService executorService;

	public MenuService(MenuMapper menuMapper, RoleMenuService roleMenuService, CommonExecutorService executorService) {
		this.menuMapper = menuMapper;
		this.roleMenuService = roleMenuService;
		this.executorService = executorService;
	}

	/**
	 * 返回用户的树形菜单集合
	 */
	@Cacheable(value = UserCacheName.USER_MENU, key = "#type + '_' + #identifier + '_' + #buildTree")
	public List<MenuDto> findUserMenu(Byte type, String identifier, String tenantCode, boolean buildTree) {
		List<Menu> userMenus = null;
		// 超级管理员
		if (SysUtil.isAdmin(identifier)) {
			Menu condition = new Menu();
			condition.setTenantCode(tenantCode);
			condition.setType(type);
			userMenus = findAllList(condition);
		} else {
			Collection<? extends GrantedAuthority> authorities = SysUtil.getAuthorities();
			if (CollectionUtils.isNotEmpty(authorities)) {
				List<Role> roleList = authorities.stream()
						// 按角色过滤
						.filter(authority -> authority.getAuthority() != null && authority.getAuthority()
								.startsWith(MenuConstant.ROLE_PREFIX)).map(authority -> {
							Role role = new Role();
							role.setRoleCode(authority.getAuthority());
							return role;
						}).collect(Collectors.toList());
				// 根据角色code查找菜单
				userMenus = finMenuByRoleList(roleList, tenantCode);
			}
		}
		return toMenuDto(userMenus, type, buildTree);
	}

	/**
	 * 根据租户标识查询
	 * @param tenantCode tenantCode
	 * @param buildTree buildTree
	 * @return List
	 */
	@Cacheable(value = UserCacheName.TENANT_MENU, key = "#tenantCode")
	public List<MenuDto> findDefaultTenantMenu(String tenantCode, boolean buildTree) {
		Menu condition = new Menu();
		condition.setTenantCode(tenantCode);
		return toMenuDto(findAllList(condition), null, buildTree);
	}

	public List<MenuDto> toMenuDto(List<Menu> menus, Byte type, boolean buildTree) {
		List<MenuDto> menuDtoList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(menus)) {
			for (Menu menu : menus) {
				if (type != null) {
					if (type.equals(menu.getType())) {
						menuDtoList.add(new MenuDto(menu));
					}
				} else {
					menuDtoList.add(new MenuDto(menu));
				}
			}
			CollUtil.sort(menuDtoList, Comparator.comparingInt(MenuDto::getSort));
			if (buildTree) {
				return TreeUtil.buildTree(menuDtoList, CommonConstant.ROOT);
			}
			return menuDtoList;
		}
		return Lists.newArrayList();
	}

	/**
	 * 根据角色查找菜单
	 *
	 * @param role       角色标识
	 */
	@Cacheable(value = UserCacheName.ROLE_MENU, key = "#role")
	public List<Menu> findMenuByRole(String role, String tenantCode) {
		return menuMapper.findByRole(role, tenantCode);
	}

	/**
	 * 批量并发查询菜单
	 *
	 * @param roleList   roleList
	 * @param tenantCode tenantCode
	 */
	private List<Menu> finMenuByRoleList(List<Role> roleList, String tenantCode) {
		List<ListenableFuture<List<Menu>>> futures = Lists.newArrayListWithExpectedSize(roleList.size());
		for (Role role : roleList) {
			ListenableFuture<List<Menu>> future = executorService.getCommonExecutor().submit(() -> {
				try {
					return findMenuByRole(role.getRoleCode(), tenantCode);
				} catch (Exception e) {
					log.error("findMenuByRole failed", e);
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
			log.error("finMenuByRoleList failed", e);
		}
		return menus;
	}

	/**
	 * 查询全部菜单
	 */
	@Override
	@Cacheable(value = UserCacheName.ALL_MENU, key = "#menu.tenantCode")
	public List<Menu> findAllList(Menu menu) {
		Menu condition = new Menu();
		condition.setTenantCode(SysUtil.getTenantCode());
		condition.setHideMenu(menu.getHideMenu());
		return menuMapper.findList(condition);
	}

	@Transactional
	@Override
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
	@Override
	@Transactional
	@CacheEvict(value = {UserCacheName.MENU, UserCacheName.ALL_MENU, UserCacheName.USER_MENU}, allEntries = true)
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
	@CacheEvict(value = {UserCacheName.MENU, UserCacheName.ALL_MENU, UserCacheName.USER_MENU}, allEntries = true)
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
	@CacheEvict(value = {UserCacheName.ALL_MENU, UserCacheName.USER_MENU}, allEntries = true)
	public int deleteByTenantCode(Menu menu) {
		return this.dao.deleteByTenantCode(menu);
	}

	/**
	 * 返回树形菜单集合
	 */
	public List<MenuDto> menuTree(String tenantCode) {
		Menu condition = new Menu();
		condition.setTenantCode(tenantCode);
		Stream<Menu> menuStream = findAllList(condition).stream();
		if (Optional.ofNullable(menuStream).isPresent()) {
			List<MenuDto> menuDtoList = menuStream.map(MenuDto::new).collect(Collectors.toList());
			return TreeUtil.buildTree(CollUtil.sort(menuDtoList, Comparator.comparingInt(MenuDto::getSort)),
					CommonConstant.ROOT);
		}
		return new ArrayList<>();
	}

	/**
	 * 复制菜单树
	 */
	@Transactional
	public int copyMenuTree(Long[] menuIds, Role role, String identifier, String tenantCode) {
		AtomicInteger counter = new AtomicInteger(0);
		List<Menu> menus = findListById(menuIds);
		if (CollectionUtils.isEmpty(menus)) {
			return counter.get();
		}
		List<MenuDto> menuDtoList = menus.stream().map(MenuDto::new).collect(Collectors.toList());
		List<MenuDto> tree = TreeUtil.buildTree(CollUtil.sort(menuDtoList, Comparator.comparingInt(MenuDto::getSort)),
				CommonConstant.ROOT);
		for (MenuDto oneDto : tree) {
			Menu oneMenu = toMenu(oneDto, identifier, tenantCode);
			// 保存一级菜单
			saveRoleMenu(oneMenu, role, identifier, tenantCode, counter);
			if (CollectionUtils.isNotEmpty(oneDto.getChildren())) {
				saveSecondAndThreeMenu(oneDto, oneMenu, role, identifier, tenantCode, counter);
			}
		}
		return counter.get();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public void saveSecondAndThreeMenu(MenuDto oneDto, Menu oneMenu, Role role, String identifier, String tenantCode,
			AtomicInteger counter) {
		for (TreeEntity<MenuDto> child : oneDto.getChildren()) {
			MenuDto secondDto = (MenuDto) child;
			Menu secondMenu = toMenu(secondDto, identifier, tenantCode);
			secondMenu.setParentId(oneMenu.getId());
			// 保存二级菜单
			saveRoleMenu(secondMenu, role, identifier, tenantCode, counter);
			if (CollectionUtils.isNotEmpty(secondDto.getChildren())) {
				for (TreeEntity<MenuDto> secondChild : secondDto.getChildren()) {
					MenuDto threeDto = (MenuDto) secondChild;
					Menu threeMenu = toMenu(threeDto, identifier, tenantCode);
					threeMenu.setParentId(secondMenu.getId());
					// 保存三级菜单
					saveRoleMenu(threeMenu, role, identifier, tenantCode, counter);
				}
			}
		}
	}

	public Menu toMenu(MenuDto dto, String identifier, String tenantCode) {
		Menu menu = new Menu();
		BeanUtils.copyProperties(dto, menu);
		menu.setNewRecord(true);
		menu.setCommonValue(identifier, tenantCode);
		menu.setId(null);
		return menu;
	}

	/**
	 * 保存菜单
	 */
	@Transactional
	public void saveRoleMenu(Menu menu, Role role, String identifier, String tenantCode, AtomicInteger counter) {
		if (insert(menu) > 0) {
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setRoleId(role.getId());
			roleMenu.setMenuId(menu.getId());
			roleMenu.setCommonValue(identifier, tenantCode);
			// 保存角色菜单关系
			roleMenuService.insert(roleMenu);
			counter.incrementAndGet();
		}
	}
}
