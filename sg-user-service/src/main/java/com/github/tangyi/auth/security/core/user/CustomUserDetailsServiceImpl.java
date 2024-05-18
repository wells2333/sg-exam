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

import com.github.tangyi.api.other.model.WxSession;
import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.service.IIdentifyService;
import com.github.tangyi.auth.security.mobile.MobileUser;
import com.github.tangyi.auth.security.wx.WxUser;
import com.github.tangyi.auth.service.WxSessionService;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.enums.LoginTypeEnum;
import com.github.tangyi.common.exceptions.TenantNotFoundException;
import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.TenantHolder;
import com.github.tangyi.common.utils.TxUtil;
import com.github.tangyi.common.vo.UserVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

	private final PlatformTransactionManager txManager;
	private final IIdentifyService identifyService;
	private final WxSessionService wxService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		long start = System.nanoTime();
		UserVo userVo = identifyService.findUserByIdentifier(null, username, TenantHolder.getTenantCode());
		checkUserExist(username, userVo);
		boolean enabled = CommonConstant.STATUS_NORMAL.equals(userVo.getStatus());
		return new CustomUserDetails(username, userVo.getCredential(), enabled, getAuthority(userVo),
				userVo.getTenantCode(), userVo.getUserId(), userVo.getPhone(), start, LoginTypeEnum.PWD);
	}

	@Override
	public UserDetails loadUserByIdentifierAndTenantCode(String tenantCode, String username)
			throws UsernameNotFoundException, TenantNotFoundException {
		long start = System.nanoTime();
		UserVo userVo = identifyService.findUserByIdentifier(null, username, tenantCode);
		checkUserExist(username, userVo);
		boolean enabled = CommonConstant.STATUS_NORMAL.equals(userVo.getStatus());
		return new CustomUserDetails(username, userVo.getCredential(), enabled, getAuthority(userVo),
				userVo.getTenantCode(), userVo.getUserId(), userVo.getPhone(), start, LoginTypeEnum.PWD);
	}

	@Override
	public UserDetails loadUserBySocialAndTenantCode(String tenantCode, String social, MobileUser mobileUser)
			throws UsernameNotFoundException {
		long start = System.nanoTime();
		UserVo userVo = socialLogin(mobileUser, social, tenantCode);
		boolean enabled = CommonConstant.STATUS_NORMAL.equals(userVo.getStatus());
		return new CustomUserDetails(userVo.getIdentifier(), userVo.getCredential(), enabled, getAuthority(userVo),
				userVo.getTenantCode(), userVo.getUserId(), userVo.getPhone(), start, LoginTypeEnum.SMS);
	}

	@Override
	public UserDetails loadUserByWxCodeAndTenantCode(String tenantCode, String code, WxUser wxUser)
			throws UsernameNotFoundException {
		WxSession wxSession = wxService.code2Session(code);
		SgPreconditions.checkNull(wxSession, "Failed to get openId");
		return loadUserByWxOpenIdAndTenantCode(wxUser, wxSession.getOpenId(), tenantCode);
	}

	@Override
	public CustomUserDetails loadUserByWxOpenIdAndTenantCode(WxUser wxUser, String openId, String tenantCode) {
		UserVo userVo = identifyService.findUserByIdentifier(IdentityType.WE_CHAT.getValue(), openId, tenantCode);
		// 第一次登录
		if (userVo == null) {
			userVo = registerUser(wxUser, openId, tenantCode);
		}
		return toCustomUserDetails(userVo);
	}

	@Override
	public UserVo registerUser(WxUser wxUser, String openId, String tenantCode) {
		UserDto userDto = new UserDto(null);
		if (wxUser != null) {
			BeanUtils.copyProperties(wxUser, userDto);
		}
		userDto.setIdentifier(openId);
		userDto.setCredential(openId);
		userDto.setIdentityType(IdentityType.WE_CHAT.getValue());
		userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
		identifyService.register(userDto);
		return identifyService.findUserByIdentifier(IdentityType.WE_CHAT.getValue(), openId, tenantCode);
	}

	@Override
	public CustomUserDetails toCustomUserDetails(UserVo userVo) {
		long start = System.currentTimeMillis();
		return new CustomUserDetails(userVo.getIdentifier(), userVo.getCredential(),
				CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), userVo.getTenantCode(),
				userVo.getUserId(), userVo.getPhone(), start, LoginTypeEnum.WECHAT);
	}

	private void checkUserExist(String username, UserVo userVo) {
		if (userVo == null) {
			throw new UsernameNotFoundException("User " + username + " does not exist");
		}
	}

	private Set<GrantedAuthority> getAuthority(UserVo userVo) {
		return userVo.getRoleList().stream().map(role -> new GrantedAuthorityImpl(role.getRoleCode().toUpperCase()))
				.collect(Collectors.toSet());
	}

	private UserVo socialLogin(MobileUser mobileUser, String social, String tenantCode) {
		UserVo vo = identifyService.findUserByIdentifier(IdentityType.PHONE_NUMBER.getValue(), social, tenantCode);
		// 第一次登录，自动注册手机号账号
		if (vo == null) {
			registerSocial(mobileUser, social, tenantCode);
			vo = identifyService.findUserByIdentifier(IdentityType.PHONE_NUMBER.getValue(), social, tenantCode);
		}
		if (vo == null) {
			throw new UsernameNotFoundException("Login by social not found: " + social);
		}

		return vo;
	}

	private void registerSocial(MobileUser mobileUser, String social, String tenantCode) {
		User user = identifyService.findUserByPhone(social, tenantCode);
		if (user == null) {
			UserDto dto = new UserDto(null);
			if (mobileUser != null) {
				BeanUtils.copyProperties(mobileUser, dto);
			}
			dto.setIdentifier(social);
			dto.setCredential(social);
			dto.setPhone(social);
			dto.setIdentityType(IdentityType.PHONE_NUMBER.getValue());
			dto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
			identifyService.register(dto);
			return;
		}

		TransactionStatus status = TxUtil.startTransaction(txManager);
		try {
			identifyService.registerUserAuths(user, social, IdentityType.PHONE_NUMBER.getValue(), social);
			txManager.commit(status);
		} catch (Exception e) {
			txManager.rollback(status);
			throw e;
		}
	}
}

