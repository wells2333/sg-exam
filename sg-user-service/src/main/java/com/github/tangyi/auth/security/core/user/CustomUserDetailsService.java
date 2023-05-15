package com.github.tangyi.auth.security.core.user;

import com.github.tangyi.auth.security.mobile.MobileUser;
import com.github.tangyi.auth.security.wx.WxUser;
import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.vo.UserVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 查询用户信息接口
 */
public interface CustomUserDetailsService extends UserDetailsService {

    /**
     * 根据用户名和租户标识查询
     */
    UserDetails loadUserByIdentifierAndTenantCode(String tenantCode, String username) throws UsernameNotFoundException;

    /**
     * 根据手机号和租户标识查询
     */
    UserDetails loadUserBySocialAndTenantCode(String tenantCode, String social, MobileUser mobileUser) throws UsernameNotFoundException;

    /**
	 * 根据微信 code 和租户标识查询
	 * 将 code 换成 openId 和 sessionKey
     */
    UserDetails loadUserByWxCodeAndTenantCode(String tenantCode, String code, WxUser wxUser) throws UsernameNotFoundException;

	/**
	 * 根据 openId 查询用户信息
	 */
	CustomUserDetails loadUserByWxOpenIdAndTenantCode(WxUser wxUser, String openId, String tenantCode);

	/**
	 * 注册用户，返回注册成功的信息
	 */
	UserVo registerUser(WxUser wxUser, String openId, String tenantCode);

	/**
	 * 转成 CustomUserDetails
	 */
	CustomUserDetails toCustomUserDetails(UserVo userVo);
}

