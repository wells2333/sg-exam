package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.RoleMenu;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuMapper extends CrudMapper<RoleMenu> {

	int deleteByRoleId(Long roleId);

	int deleteByMenuId(Long menuId);

	int insertBatch(List<RoleMenu> roleMenus);

	List<RoleMenu> getByRoleId(Long roleId);

	List<RoleMenu> getByMenuId(RoleMenu roleMenu);

	List<RoleMenu> getByMenuIds(List<RoleMenu> roleMenus);
}
