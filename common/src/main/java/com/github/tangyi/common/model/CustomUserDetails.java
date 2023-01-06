package com.github.tangyi.common.model;

import com.github.tangyi.common.enums.LoginTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CustomUserDetails extends User {

	private static final long serialVersionUID = 1L;

	/**
	 * 租户标识
	 */
	private String tenantCode;

	/**
	 * userId
	 */
	private Long id;

	/**
	 * 开始授权时间
	 */
	private long start;

	/**
	 * 登录类型
	 */
	private LoginTypeEnum loginType;

	public CustomUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities,
			String tenantCode) {
		super(username, password, authorities);
		this.id = id;
		this.tenantCode = tenantCode;
	}

	/**
	 * 构造方法
	 *
	 * @param username    username
	 * @param password    password
	 * @param enabled     enabled
	 * @param authorities authorities
	 * @param tenantCode  tenantCode
	 * @param start       start
	 * @param loginType   loginType
	 */
	public CustomUserDetails(String username, String password, boolean enabled,
			Collection<? extends GrantedAuthority> authorities, String tenantCode, Long id, long start,
			LoginTypeEnum loginType) {
		super(username, password, enabled, true, true, true, authorities);
		this.tenantCode = tenantCode;
		this.id = id;
		this.start = start;
		this.loginType = loginType;
	}
}
