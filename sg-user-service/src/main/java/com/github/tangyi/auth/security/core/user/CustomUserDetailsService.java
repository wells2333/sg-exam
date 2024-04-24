/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

