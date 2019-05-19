package com.github.tangyi.user.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 角色菜单关联
 *
 * @author tangyi
 * @date 2018/8/26 22:24
 */
@Data
public class RoleMenu extends BaseEntity<RoleMenu> {

    private String roleId;

    private String menuId;
}
