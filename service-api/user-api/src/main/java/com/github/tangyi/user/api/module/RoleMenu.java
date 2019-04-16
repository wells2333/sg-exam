package com.github.tangyi.user.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;

/**
 * 角色菜单关联
 *
 * @author tangyi
 * @date 2018/8/26 22:24
 */
public class RoleMenu extends BaseEntity<RoleMenu> {

    private String roleId;

    private String menuId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
