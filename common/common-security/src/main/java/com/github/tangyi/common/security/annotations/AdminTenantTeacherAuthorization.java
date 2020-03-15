package com.github.tangyi.common.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

/**
 * 租户或超管权限
 *
 * @author tangyi
 * @date 2019/11/02 12:40
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@PreAuthorize("hasRole(T(com.github.tangyi.common.security.enums.Roles).ROLE_ADMIN) or hasRole(T(com.github.tangyi.common.security.enums.Roles).ROLE_TENANT_ADMIN) or hasRole(T(com.github.tangyi.common.security.enums.Roles).ROLE_TEACHER) or hasRole(T(com.github.tangyi.common.security.enums.Roles).ROLE_PREVIEW)")
public @interface AdminTenantTeacherAuthorization {
}
