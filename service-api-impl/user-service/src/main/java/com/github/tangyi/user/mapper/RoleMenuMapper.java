package com.github.tangyi.user.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;

import com.github.tangyi.user.api.module.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色菜单mapper
 *
 * @author tangyi
 * @date 2018/8/26 22:34
 */
@Mapper
public interface RoleMenuMapper extends CrudMapper<RoleMenu> {

    /**
     * 根据角色ID删除
     *
     * @param roleId 角色ID
     * @return int
     */
    int deleteByRoleId(String roleId);

    /**
     * 根据菜单ID删除
     *
     * @param menuId 菜单ID
     * @return int
     */
    int deleteByMenuId(String menuId);

    /**
     * 批量保存
     *
     * @param roleMenus roleMenus
     * @return int
     */
    int insertBatch(List<RoleMenu> roleMenus);
}
