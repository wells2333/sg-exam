/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.auth.security.wx;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 微信登录 token
 */
public class WxAuthenticationToken extends AbstractAuthenticationToken {

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
