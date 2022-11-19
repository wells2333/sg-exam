package com.github.tangyi.common.utils;

import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.security.JwtAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * 系统工具类
 *
 * @author tangyi
 * @date 2018-09-13 20:50
 */
@Slf4j
public class SysUtil {

	public static final String ADMIN_IDENTIFIER = EnvUtils.getValue("ADMIN_IDENTIFIER", "admin");

	public static boolean isAdmin(String identifier) {
		return ADMIN_IDENTIFIER.equals(identifier);
	}

	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取当前登录的用户名
	 *
	 * @return String
	 * @author tangyi
	 * @date 2019/03/17 11:46
	 */
	public static String getUser() {
		return getAuthentication().getName();
	}

	/**
	 * 获取当前用户的userId
	 * @return Long
	 */
	public static Long getUserId() {
		JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) getAuthentication();
		if (authenticationToken != null) {
			CustomUserDetails details = (CustomUserDetails) authenticationToken.getPrincipal();
			if (details != null) {
				return details.getId();
			}
		}
		return null;
	}

	public static Collection<? extends GrantedAuthority> getAuthorities() {
		return getAuthentication().getAuthorities();
	}

	/**
	 * 获取租户编号
	 *
	 * @return String
	 */
	public static String getTenantCode() {
		String tenantCode = TenantContextHolder.getTenantCode();
		if (StringUtils.isBlank(tenantCode)) {
			throw new IllegalArgumentException("tenant code is blank");
		}
		return tenantCode;
	}
}
