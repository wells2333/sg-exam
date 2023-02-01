package com.github.tangyi.user.service;

import cn.hutool.core.collection.CollUtil;
import com.github.tangyi.api.user.constant.UserServiceConstant;
import com.github.tangyi.api.user.dto.MenuDto;
import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.api.user.model.RoleMenu;
import com.github.tangyi.api.user.service.IMenuService;
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

@Slf4j
@Service
public class MenuService extends CrudService<MenuMapper, Menu> implements IMenuService {

	private final MenuMapper menuMapper;

	private final RoleMenuService roleMenuService;

	private final CommonExecutorService executorService;

	public MenuService(MenuMapper menuMapper, RoleMenuService roleMenuService, CommonExecutorService executorService) {
		this.menuMapper = menuMapper;
		this.roleMenuService = roleMenuService;
		this.executorService = executorService;
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

	@Cacheable(value = UserCacheName.USER_MENU, key = "#tenantCode + '_' + #type + '_' + #identifier + '_' + #buildTree")
	public List<MenuDto> findUserMenuTree(Byte type, String identifier, String tenantCode, boolean buildTree) {
		List<Menu> userMenus = SysUtil.isAdmin(identifier) ?
				findAdminMenus(type, tenantCode) :
				findUserMenus(tenantCode);
		return toMenuDto(userMenus, type, buildTree);
	}

	@Cacheable(value = UserCacheName.TENANT_MENU, key = "#tenantCode")
	public List<MenuDto> findDefaultTenantMenu(String tenantCode, boolean buildTree) {
		Menu condition = new Menu();
		condition.setTenantCode(tenantCode);
		return toMenuDto(findAllList(condition), null, buildTree);
	}

	@Cacheable(value = UserCacheName.ROLE_MENU, key = "#role")
	public List<Menu> findMenuByRole(String role, String tenantCode) {
		return menuMapper.findByRole(role, tenantCode);
	}

	@Override
	@Cacheable(value = UserCacheName.ALL_MENU, key = "#menu.tenantCode")
	public List<Menu> findAllList(Menu menu) {
		Menu condition = new Menu();
		condition.setTenantCode(SysUtil.getTenantCode());
		condition.setHideMenu(menu.getHideMenu());
		return menuMapper.findList(condition);
	}

	@Override
	@Transactional
	@CacheEvict(value = {UserCacheName.MENU, UserCacheName.ALL_MENU, UserCacheName.USER_MENU,
			UserCacheName.ROLE_MENU}, allEntries = true)
	public int insert(Menu menu) {
		return super.insert(menu);
	}

	/**
	 * 更新菜单，区分租户本身的菜单和默认租户的菜单
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

	@Transactional
	public int copyMenuTree(Long[] menuIds, Role role, String identifier, String tenantCode) {
		AtomicInteger counter = new AtomicInteger(0);
		List<Menu> menus = findListById(menuIds);
		if (CollectionUtils.isEmpty(menus)) {
			return counter.get();
		}
		List<MenuDto> list = menus.stream().map(MenuDto::new).collect(Collectors.toList());
		List<MenuDto> tree = TreeUtil.buildTree(CollUtil.sort(list, Comparator.comparingInt(MenuDto::getSort)),
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
			ListenableFuture<List<Menu>> future = executorService.getCommonExecutor().submit(() -> {
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
			return Collections.emptyList();
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

	private List<MenuDto> toMenuDto(List<Menu> menus, Byte type, boolean buildTree) {
		if (CollectionUtils.isEmpty(menus)) {
			return Collections.emptyList();
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

	private Menu toMenu(MenuDto dto, String identifier, String tenantCode) {
		Menu menu = new Menu();
		BeanUtils.copyProperties(dto, menu);
		menu.setNewRecord(true);
		menu.setCommonValue(identifier, tenantCode);
		menu.setId(null);
		return menu;
	}
}
