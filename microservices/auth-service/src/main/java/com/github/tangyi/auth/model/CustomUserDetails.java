package com.github.tangyi.auth.model;

import com.github.tangyi.common.enums.LoginTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
public class CustomUserDetails extends User {

    private static final long serialVersionUID = 1L;

    /**
     * 租户标识
     */
    private String tenantCode;

    /**
     * id
     */
    private Long id;

    /**
     * 开始授权时间
     */
    private long start;

    /**
     * 登录类型
     */
    private LoginTypeEnum loginType;

    /**
     * 构造方法
     *
     * @param username    username
     * @param password    password
     * @param enabled     enabled
     * @param authorities authorities
     * @param tenantCode  tenantCode
     * @param start       start
     * @param loginType   loginType
     */
    public CustomUserDetails(String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities, String tenantCode, Long id, long start, LoginTypeEnum loginType) {
        super(username, password, enabled, true, true, true, authorities);
        this.tenantCode = tenantCode;
        this.id = id;
        this.start = start;
        this.loginType = loginType;
    }
}
