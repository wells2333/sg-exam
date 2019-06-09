package com.github.tangyi.auth.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 用户信息
 *
 * @author tangyi
 * @date 2019/5/28 21:13
 */
@Data
public class CustomUserDetails extends User {

    private static final long serialVersionUID = 1L;

    private String tenantCode;

    /**
     * 构造方法
     *
     * @param username    username
     * @param password    password
     * @param enabled     enabled
     * @param authorities authorities
     * @param tenantCode  tenantCode
     */
    public CustomUserDetails(String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities, String tenantCode) {
        super(username, password, enabled, true, true, true, authorities);
        this.tenantCode = tenantCode;
    }
}
