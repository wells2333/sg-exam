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
	 * 根据微信code和租户标识查询
	 * 将code换成openId和sessionKey
     *
     * @param tenantCode tenantCode
     * @param code       code
     * @param wxUser     wxUser
     * @return UserDetails
     * @author tangyi
     * @date 2019/07/05 20:04:59
     */
    UserDetails loadUserByWxCodeAndTenantCode(String tenantCode, String code, WxUser wxUser) throws UsernameNotFoundException;

	/**
	 * 根据openId查询用户信息
	 * @param wxUser wxUser
	 * @param openId openId
	 * @param tenantCode tenantCode
	 * @return CustomUserDetails
	 */
	CustomUserDetails loadUserByWxOpenIdAndTenantCode(WxUser wxUser, String openId, String tenantCode);

	/**
	 * 注册用户，返回注册成功的信息
	 * @param wxUser wxUser
	 * @param openId openId
	 * @param tenantCode tenantCode
	 * @return UserVo
	 */
	UserVo registerUser(WxUser wxUser, String openId, String tenantCode);

	/**
	 * 转成CustomUserDetails
	 * @return CustomUserDetails
	 */
	CustomUserDetails toCustomUserDetails(UserVo userVo);
}

