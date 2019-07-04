package com.github.tangyi.user.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 角色
 *
 * @author tangyi
 * @date 2018-08-25 13:58
 */
@Data
public class Role extends BaseEntity<Role> {

    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @NotBlank(message = "角色标识不能为空")
    private String roleCode;

    private String roleDesc;

    private Integer status;

    private String deptName;

    private String menuIds;

    /**
     * 是否默认 0-否，1-是
     */
    private Integer isDefault;
}
