package com.github.tangyi.common.utils;

import com.github.tangyi.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

import java.security.Principal;

/**
 * 系统工具类
 *
 * @author tangyi
 * @date 2018-09-13 20:50
 */
@Slf4j
public class SysUtil {

	/**
	 * 获取当前登录的用户名
	 *
	 * @return String
	 * @author tangyi
	 * @date 2019/03/17 11:46
	 */
	public static String getUser() {
		try {
			SecurityContext sc = SecurityContextHolder.getContext();
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			log.info("principal: {}", principal.toString());
			if (principal instanceof Jwt) {
				Jwt jwt = (Jwt) principal;
				return (String) jwt.getClaims().getOrDefault(CommonConstant.USER_NAME, "");
			}
			if (principal instanceof UserDetails)
				return ((UserDetails) principal).getUsername();
			if (principal instanceof Principal)
				return ((Principal) principal).getName();
			return String.valueOf(principal);
		} catch (Exception e) {
			log.error("get user error: {}", e.getMessage(), e);
		}
		return "";
	}

	/**
	 * 获取系统编号
	 *
	 * @return String
	 */
	public static String getSysCode() {
		return CommonConstant.SYS_CODE;
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

	/**
	 * 获取当前用户的授权信息
	 *
	 * @return Authentication
	 * @author tangyi
	 * @date 2019/03/17 19:18
	 */
	public static Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取当前登录用户的授权信息
	 *
	 * @return Object
	 * @author tangyi
	 * @date 2019/03/17 11:48
	 */
	public static Object getCurrentPrincipal() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
