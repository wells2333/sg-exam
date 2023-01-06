package com.github.tangyi.auth.security.wx;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 微信登录token
 */
public class WxAuthenticationToken extends AbstractAuthenticationToken {

	/**
	 * code
	 */
	private final Object code;

	private final String tenantCode;

	private WxUser wxUser;

	public WxAuthenticationToken(String tenantCode, String code) {
		super(null);
		this.tenantCode = tenantCode;
		this.code = code;
		setAuthenticated(false);
	}

	public WxAuthenticationToken(String tenantCode, Object code, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.tenantCode = tenantCode;
		this.code = code;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.code;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}
		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
	}

	public WxUser getWxUser() {
		return wxUser;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setWxUser(WxUser wxUser) {
		this.wxUser = wxUser;
	}
}
