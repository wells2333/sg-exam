package com.github.tangyi.auth.security.core.user;

import com.github.tangyi.api.other.model.WxSession;
import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.model.UserAuths;
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
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

	private final UserService userService;

	private final WxSessionService wxService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		long start = System.nanoTime();
		UserVo userVo = userService.findUserByIdentifier(null, username, TenantHolder.getTenantCode());
		checkUserExist(userVo);
		boolean enabled = CommonConstant.STATUS_NORMAL.equals(userVo.getStatus());
		return new CustomUserDetails(username, userVo.getCredential(), enabled, getAuthority(userVo),
				userVo.getTenantCode(), userVo.getUserId(), userVo.getPhone(), start, LoginTypeEnum.PWD);
	}

	@Override
	public UserDetails loadUserByIdentifierAndTenantCode(String tenantCode, String username)
			throws UsernameNotFoundException, TenantNotFoundException {
		long start = System.nanoTime();
		UserVo userVo = userService.findUserByIdentifier(null, username, tenantCode);
		checkUserExist(userVo);
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
		UserVo userVo = userService.findUserByIdentifier(IdentityType.WE_CHAT.getValue(), openId, tenantCode);
		// 第一次登录
		if (userVo == null) {
			userVo = registerUser(wxUser, openId, tenantCode);
		}
		return toCustomUserDetails(userVo);
	}

	public UserVo registerUser(WxUser wxUser, String openId, String tenantCode) {
		UserDto userDto = new UserDto(null);
		if (wxUser != null) {
			BeanUtils.copyProperties(wxUser, userDto);
		}
		userDto.setIdentifier(openId);
		userDto.setCredential(openId);
		userDto.setIdentityType(IdentityType.WE_CHAT.getValue());
		userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
		userService.register(userDto);
		return userService.findUserByIdentifier(IdentityType.WE_CHAT.getValue(), openId, tenantCode);
	}

	@Override
	public CustomUserDetails toCustomUserDetails(UserVo userVo) {
		long start = System.currentTimeMillis();
		return new CustomUserDetails(userVo.getIdentifier(), userVo.getCredential(),
				CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), userVo.getTenantCode(),
				userVo.getUserId(), userVo.getPhone(), start, LoginTypeEnum.WECHAT);
	}

	private void checkUserExist(UserVo userVo) {
		if (userVo == null) {
			throw new UsernameNotFoundException("User does not exist");
		}
	}

	private Set<GrantedAuthority> getAuthority(UserVo userVo) {
		return userVo.getRoleList().stream().map(role -> new GrantedAuthorityImpl(role.getRoleCode().toUpperCase()))
				.collect(Collectors.toSet());
	}

	private UserVo socialLogin(MobileUser mobileUser, String social, String tenantCode) {
		UserVo userVo = userService.findUserByIdentifier(IdentityType.PHONE_NUMBER.getValue(), social, tenantCode);
		// 第一次登录，自动注册手机号账号
		if (userVo == null) {
			registerSocial(mobileUser, social, tenantCode);
			userVo = userService.findUserByIdentifier(IdentityType.PHONE_NUMBER.getValue(), social, tenantCode);
		}
		if (userVo == null) {
			throw new UsernameNotFoundException("Login by social not found: " + social);
		}
		return userVo;
	}

	private void registerSocial(MobileUser mobileUser, String social, String tenantCode) {
		User user = userService.findUserByPhone(social, tenantCode);
		if (user == null) {
			UserDto userDto = new UserDto(null);
			if (mobileUser != null) {
				BeanUtils.copyProperties(mobileUser, userDto);
			}
			userDto.setIdentifier(social);
			userDto.setCredential(social);
			userDto.setPhone(social);
			userDto.setIdentityType(IdentityType.PHONE_NUMBER.getValue());
			userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
			userService.register(userDto);
		} else {
			userService.registerUserAuths(user, social, IdentityType.PHONE_NUMBER.getValue(), social);
		}
	}
}

