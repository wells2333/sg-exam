package com.github.tangyi.user.service;

import com.github.tangyi.api.user.module.RoleMenu;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.IdGen;
import com.github.tangyi.user.mapper.RoleMenuMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangyi
 * @date 2018/8/26 22:47
 */
@AllArgsConstructor
@Service
public class RoleMenuService extends CrudService<RoleMenuMapper, RoleMenu> {

	private final RoleMenuMapper roleMenuMapper;

	/**
	 * @param role  role
	 * @param menus 菜单ID集合
	 * @return int
	 * @author tangyi
	 * @date 2018/10/28 14:29
	 */
	@Transactional
	@CacheEvict(value = "menu", allEntries = true)
	public int saveRoleMenus(Long role, List<Long> menus) {
		int update = -1;
		if (CollectionUtils.isNotEmpty(menus)) {
			// 删除旧的管理数据
			roleMenuMapper.deleteByRoleId(role);
			List<RoleMenu> roleMenus = new ArrayList<>();
			for (Long menuId : menus) {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setId(IdGen.snowflakeId());
				roleMenu.setRoleId(role);
				roleMenu.setMenuId(menuId);
				roleMenus.add(roleMenu);
			}
			update = roleMenuMapper.insertBatch(roleMenus);
		}
		return update;
	}

	/**
	 * 批量保存
	 *
	 * @param roleMenus roleMenus
	 * @return int
	 * @author tangyi
	 * @date 2018/10/30 19:59
	 */
	@Transactional
	public int insertBatch(List<RoleMenu> roleMenus) {
		return roleMenuMapper.insertBatch(roleMenus);
	}

	/**
	 * 根据roleId查询
	 *
	 * @param roleMenu roleMenu
	 * @return List
	 * @author tangyi
	 * @date 2019/09/02 22:22:12
	 */
	public List<RoleMenu> getByRoleId(RoleMenu roleMenu) {
		return roleMenuMapper.getByRoleId(roleMenu);
	}

	/**
	 * 根据menuId查询
	 *
	 * @param roleMenu roleMenu
	 * @return List
	 * @author tangyi
	 * @date 2019-09-14 15:49
	 */
	public List<RoleMenu> getByMenuId(RoleMenu roleMenu) {
		return roleMenuMapper.getByMenuId(roleMenu);
	}

	/**
	 * 根据menuId列表查询
	 *
	 * @param roleMenus roleMenus
	 * @return List
	 * @author tangyi
	 * @date 2019-09-14 16:00
	 */
	public List<RoleMenu> getByMenuIds(List<RoleMenu> roleMenus) {
		return roleMenuMapper.getByMenuIds(roleMenus);
	}
}
