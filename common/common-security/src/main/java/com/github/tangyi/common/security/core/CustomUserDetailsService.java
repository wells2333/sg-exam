package com.github.tangyi.common.security.core;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 查询用户信息接口
 *
 * @author tangyi
 * @date 2019/5/28 21:05
 */
public interface CustomUserDetailsService {

    /**
     * 根据用户名和租户标识查询
     *
     * @param username   username
     * @param tenantCode tenantCode
     * @return UserDetails
     * @author tangyi
     * @date 2019/05/28 21:06
     */
    UserDetails loadUserByUsernameAndTenantCode(String username, String tenantCode) throws UsernameNotFoundException;

    /**
     * 根据社交账号和租户标识查询
     *
     * @param social social
     * @param tenantCode tenantCode
     * @return UserDetails
     * @author tangyi
     * @date 2019/06/22 21:08
     */
    UserDetails loadUserBySocialAndTenantCode(String social, String tenantCode) throws UsernameNotFoundException;
}