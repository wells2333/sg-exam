package com.github.tangyi.auth.security.core;

import com.github.tangyi.auth.security.mobile.MobileUser;
import com.github.tangyi.auth.security.wx.WxUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 查询用户信息接口
 *
 * @author tangyi
 * @date 2019/5/28 21:05
 */
public interface CustomUserDetailsService extends UserDetailsService {

    /**
     * 根据用户名和租户标识查询
     *
     * @param tenantCode tenantCode
     * @param username   username
     * @return UserDetails
     * @author tangyi
     * @date 2019/05/28 21:06
     */
    UserDetails loadUserByIdentifierAndTenantCode(String tenantCode, String username) throws UsernameNotFoundException;

    /**
     * 根据社交账号和租户标识查询
     *
     * @param tenantCode tenantCode
     * @param social     social
     * @param mobileUser mobileUser
     * @return UserDetails
     * @author tangyi
     * @date 2019/06/22 21:08
     */
    UserDetails loadUserBySocialAndTenantCode(String tenantCode, String social, MobileUser mobileUser) throws UsernameNotFoundException;

    /**
     * 根据微信openId和租户标识查询
     *
     * @param tenantCode tenantCode
     * @param code       code
     * @param wxUser     wxUser
     * @return UserDetails
     * @author tangyi
     * @date 2019/07/05 20:04:59
     */
    UserDetails loadUserByWxCodeAndTenantCode(String tenantCode, String code, WxUser wxUser) throws UsernameNotFoundException;
}

