package com.github.tangyi.api.user.module;

import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色菜单关联
 *
 * @author tangyi
 * @date 2018/8/26 22:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleMenu extends BaseEntity<RoleMenu> {

    private Long roleId;

    private Long menuId;
}
