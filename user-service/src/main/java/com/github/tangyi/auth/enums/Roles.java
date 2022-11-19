package com.github.tangyi.auth.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * 角色枚举
 *
 * @author tangyi
 * @date 2019/11/02 12:30
 */
public enum Roles implements GrantedAuthority {
    /**
     * 普通用户
     */
    ROLE_USER,

    /**
     * 超级管理员
     */
    ROLE_ADMIN,

    /**
     * 租户管理员
     */
    ROLE_TENANT_ADMIN,

    /**
     * 老师
     */
    ROLE_TEACHER,

    /**
     * 预览角色
     */
    ROLE_PREVIEW;

    @Override
    public String getAuthority() {
        return name();
    }
}
