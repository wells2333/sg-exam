package com.github.tangyi.auth.utils;

import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.common.utils.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@Slf4j
public class SysUtil {

	/**
	 * 获取当前登录的用户名
	 */
	public static String getUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		if (principal instanceof Principal) {
			return ((Principal) principal).getName();
		}
		return String.valueOf(principal);
	}

	/**
	 * 获取系统编号
	 */
	public static String getSysCode() {
		return SecurityConstant.SYS_CODE;
	}

	/**
	 * 获取租户编号
	 */
	public static String getTenantCode() {
		String tenantCode = TenantContextHolder.getTenantCode();
		if (StringUtils.isBlank(tenantCode)) {
			tenantCode = getCurrentUserTenantCode();
		}
		if (StringUtils.isBlank(tenantCode)) {
			tenantCode = SecurityConstant.DEFAULT_TENANT_CODE;
		}
		return tenantCode;
	}

	/**
	 * 获取当前登录的租户code
	 */
	private static String getCurrentUserTenantCode() {
		String tenantCode = "";
		return tenantCode;
	}

	/**
	 * 获取当前用户的授权信息
	 */
	public static Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取当前登录用户的授权信息
	 */
	public static Object getCurrentPrincipal() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * 从header 请求中的clientId/clientsecect
	 *
	 * @param header header中的参数
	 * @throws RuntimeException if the Basic header is not present or is not valid
	 *                          Base64
	 */
	public static String[] extractAndDecodeHeader(String header) throws IOException {
		byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Failed to decode basic authentication token");
		}
		String token = new String(decoded, StandardCharsets.UTF_8);
		int delim = token.indexOf(":");
		if (delim == -1) {
			throw new RuntimeException("Invalid basic authentication token");
		}
		return new String[]{token.substring(0, delim), token.substring(delim + 1)};
	}
}
