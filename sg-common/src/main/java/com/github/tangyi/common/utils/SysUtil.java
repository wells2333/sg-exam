package com.github.tangyi.common.utils;

import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.security.JwtAuthenticationToken;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

@Slf4j
public class SysUtil {

	private static final String ADMIN_IDENTIFIER = EnvUtils.getValue("ADMIN_IDENTIFIER", "admin");

	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static boolean isAdmin(String identifier) {
		return ADMIN_IDENTIFIER.equals(identifier);
	}

	public static String getUser() {
		Authentication authentication = getAuthentication();
		Preconditions.checkNotNull(authentication, "Authentication is null");
		return authentication.getName();
	}

	public static CustomUserDetails getUserDetails() {
		JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) getAuthentication();
		if (authenticationToken != null) {
			return (CustomUserDetails) authenticationToken.getPrincipal();
		}
		return null;
	}

	public static Long getUserId() {
		CustomUserDetails details = getUserDetails();
		if (details != null) {
			return details.getId();
		}
		return null;
	}

	public static String getUserName() {
		CustomUserDetails details = getUserDetails();
		if (details != null) {
			return details.getUsername();
		}
		return "";
	}

	public static Collection<? extends GrantedAuthority> getAuthorities() {
		return getAuthentication().getAuthorities();
	}

	public static String getTenantCode() {
		String tenantCode = TenantHolder.getTenantCode();
		if (StringUtils.isBlank(tenantCode)) {
			throw new IllegalArgumentException("tenant code is blank");
		}
		return tenantCode;
	}
}
