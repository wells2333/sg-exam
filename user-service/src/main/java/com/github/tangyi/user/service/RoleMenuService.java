package com.github.tangyi.user.service;

import com.github.tangyi.api.user.model.RoleMenu;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.RoleMenuMapper;
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
public class RoleMenuService extends CrudService<RoleMenuMapper, RoleMenu> {

	private final RoleMenuMapper roleMenuMapper;

	@Transactional
	@CacheEvict(value = {UserCacheName.MENU, UserCacheName.USER_MENU}, allEntries = true)
	public int saveRoleMenus(Long roleId, String menuIds) {
		if (StringUtils.isEmpty(menuIds)) {
			return -1;
		}
		return saveRoleMenus(roleId,
				Stream.of(menuIds.split(CommonConstant.COMMA)).map(Long::parseLong).collect(Collectors.toList()));
	}

	@Transactional
	@CacheEvict(value = {UserCacheName.MENU, UserCacheName.USER_MENU}, allEntries = true)
	public int saveRoleMenus(Long role, List<Long> menus) {
		int update = -1;
		if (CollectionUtils.isNotEmpty(menus)) {
			roleMenuMapper.deleteByRoleId(role);
			List<RoleMenu> roleMenus = new ArrayList<>();
			for (Long menuId : menus) {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setRoleId(role);
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
}
