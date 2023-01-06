package com.github.tangyi.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final UserDetails userDetails;

	private final TokenManager tokenManager;

	public JwtAuthenticationToken(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities,
			TokenManager tokenManager) {
		super(authorities);
		this.userDetails = userDetails;
		this.tokenManager = tokenManager;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return userDetails;
	}
}
