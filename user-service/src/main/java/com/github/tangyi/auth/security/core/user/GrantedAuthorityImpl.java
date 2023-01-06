package com.github.tangyi.auth.security.core.user;

import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {

	private static final long serialVersionUID = -5588742812922519390L;

	private final String roleName;

	public GrantedAuthorityImpl(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String getAuthority() {
		return roleName;
	}
}

