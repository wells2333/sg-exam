package com.github.tangyi.auth.security.core;

import org.springframework.security.core.GrantedAuthority;

/**
 * GrantedAuthority封装
 *
 * @author tangyi
 * @date 2019/3/17 14:29
 */
public class GrantedAuthorityImpl implements GrantedAuthority {

	private static final long serialVersionUID = -5588742812922519390L;

	private String roleName;

	public GrantedAuthorityImpl(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String getAuthority() {
		return roleName;
	}
}

