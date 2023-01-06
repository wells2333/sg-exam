package com.github.tangyi.auth.security.core.user;

import com.github.tangyi.api.model.WxSession;
import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.auth.security.mobile.MobileUser;
import com.github.tangyi.auth.security.wx.WxUser;
import com.github.tangyi.auth.service.WxSessionService;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.enums.LoginTypeEnum;
import com.github.tangyi.common.exceptions.TenantNotFoundException;
import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.common.utils.TenantContextHolder;
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
		long start = System.currentTimeMillis();
		UserVo userVo = userService.findUserByIdentifier(null, username, TenantContextHolder.getTenantCode());
		if (userVo == null) {
			throw new UsernameNotFoundException("user does not exist");
		}
		return new CustomUserDetails(username, userVo.getCredential(),
				CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), userVo.getTenantCode(),
				userVo.getUserId(), start, LoginTypeEnum.PWD);

	}

	@Override
	public UserDetails loadUserByIdentifierAndTenantCode(String tenantCode, String username)
			throws UsernameNotFoundException, TenantNotFoundException {
		long start = System.currentTimeMillis();
		UserVo userVo = userService.findUserByIdentifier(null, username, tenantCode);
		if (userVo == null) {
			throw new UsernameNotFoundException("user does not exist");
		}
		return new CustomUserDetails(username, userVo.getCredential(),
				CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), userVo.getTenantCode(),
				userVo.getUserId(), start, LoginTypeEnum.PWD);
	}

	@Override
	public UserDetails loadUserBySocialAndTenantCode(String tenantCode, String social, MobileUser mobileUser)
			throws UsernameNotFoundException {
		long start = System.currentTimeMillis();
		UserVo userVo = userService.findUserByIdentifier(IdentityType.PHONE_NUMBER.getValue(), social, tenantCode);
		// 第一次登录
		if (userVo == null) {
			UserDto userDto = new UserDto(null);
			// 用户的基本信息
			if (mobileUser != null) {
				BeanUtils.copyProperties(mobileUser, userDto);
			}
			userDto.setIdentifier(social);
			userDto.setCredential(social);
			userDto.setIdentityType(IdentityType.PHONE_NUMBER.getValue());
			userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
			// 注册账号
			userService.register(userDto);
			// 重新获取用户信息
			userVo = userService.findUserByIdentifier(IdentityType.PHONE_NUMBER.getValue(), social, tenantCode);
		}
		return new CustomUserDetails(userVo.getIdentifier(), userVo.getCredential(),
				CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), userVo.getTenantCode(),
				userVo.getUserId(), start, LoginTypeEnum.SMS);
	}

	@Override
	public UserDetails loadUserByWxCodeAndTenantCode(String tenantCode, String code, WxUser wxUser)
			throws UsernameNotFoundException {
		// 根据code获取openId和sessionKey
		WxSession wxSession = wxService.code2Session(code);
		SgPreconditions.checkNull(wxSession, "get openId failed");
		return loadUserByWxOpenIdAndTenantCode(wxUser, wxSession.getOpenId(), tenantCode);
	}

	@Override
	public CustomUserDetails loadUserByWxOpenIdAndTenantCode(WxUser wxUser, String openId, String tenantCode) {
		// 获取用户信息
		UserVo userVo = userService.findUserByIdentifier(IdentityType.WE_CHAT.getValue(), openId, tenantCode);
		// 为空说明是第一次登录，需要将用户信息增加到数据库里
		if (userVo == null) {
			userVo = registerUser(wxUser, openId, tenantCode);
		}
		return toCustomUserDetails(userVo);
	}

	public UserVo registerUser(WxUser wxUser, String openId, String tenantCode) {
		UserDto userDto = new UserDto(null);
		// 用户的基本信息
		if (wxUser != null) {
			BeanUtils.copyProperties(wxUser, userDto);
		}
		userDto.setIdentifier(openId);
		userDto.setCredential(openId);
		userDto.setIdentityType(IdentityType.WE_CHAT.getValue());
		userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
		// 注册账号
		userService.register(userDto);
		// 重新获取用户信息
		return userService.findUserByIdentifier(IdentityType.WE_CHAT.getValue(), openId, tenantCode);
	}

	@Override
	public CustomUserDetails toCustomUserDetails(UserVo userVo) {
		long start = System.currentTimeMillis();
		return new CustomUserDetails(userVo.getIdentifier(), userVo.getCredential(),
				CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), userVo.getTenantCode(),
				userVo.getUserId(), start, LoginTypeEnum.WECHAT);
	}

	private Set<GrantedAuthority> getAuthority(UserVo userVo) {
		return userVo.getRoleList().stream().map(role -> new GrantedAuthorityImpl(role.getRoleCode().toUpperCase()))
				.collect(Collectors.toSet());
	}
}

