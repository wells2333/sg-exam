package com.github.tangyi.user.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 角色
 *
 * @author tangyi
 * @date 2018-08-25 13:58
 */
@Data
public class Role extends BaseEntity<Role> {

    private String roleName;

    private String roleCode;

    private String roleDesc;

    private String status;

    private String deptName;

    private String deptId;

    /**
     * 是否默认 0-否，1-是
     */
    private String isDefault;
}
