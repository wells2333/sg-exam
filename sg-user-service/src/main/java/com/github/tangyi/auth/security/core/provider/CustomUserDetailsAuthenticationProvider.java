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

package com.github.tangyi.auth.security.core.provider;

import com.github.tangyi.auth.security.core.event.CustomAuthenticationFailureEvent;
import com.github.tangyi.auth.security.core.event.CustomAuthenticationSuccessEvent;
import com.github.tangyi.auth.security.core.user.CustomUserDetailsService;
import com.github.tangyi.common.exceptions.TenantNotFoundException;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.github.tangyi.common.utils.TenantHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 认证 Provider，提供获取用户信息、认证、授权等功能
 */
@Slf4j
public class CustomUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService userDetailsService;
	private String userNotFoundEncodedPassword;

	public CustomUserDetailsAuthenticationProvider(PasswordEncoder passwordEncoder,
			CustomUserDetailsService userDetailsService) {
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			log.error("authentication failed, password is null");
			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "密码为空"));
		}

		String pass = authentication.getCredentials().toString();
		// 匹配密码
		if (!passwordEncoder.matches(pass, userDetails.getPassword())) {
			log.debug("Authentication failed: invalid password");
			SpringContextHolder.publishEvent(new CustomAuthenticationFailureEvent(authentication, userDetails));
			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
							"用户名或密码错误"));
		}
		SpringContextHolder.publishEvent(new CustomAuthenticationSuccessEvent(authentication, userDetails));
	}

	@Override
	protected void doAfterPropertiesSet() {
		if (this.userDetailsService == null) {
			throw new IllegalArgumentException("UserDetailsService can not be null");
		}
		this.userNotFoundEncodedPassword = this.passwordEncoder.encode("userNotFoundPassword");
	}

	/**
	 * 加载用户信息
	 */
	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException, TenantNotFoundException {
		UserDetails loadedUser;
		try {
			String tenantCode = TenantHolder.getTenantCode();
			String principal = authentication.getPrincipal().toString();
			loadedUser = this.userDetailsService.loadUserByIdentifierAndTenantCode(tenantCode, principal);
		} catch (UsernameNotFoundException notFound) {
			if (authentication.getCredentials() != null) {
				String presentedPassword = authentication.getCredentials().toString();
				passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
			}
			throw notFound;
		} catch (Exception tenantNotFound) {
			throw new InternalAuthenticationServiceException(tenantNotFound.getMessage(), tenantNotFound);
		}

		if (loadedUser == null) {
			throw new InternalAuthenticationServiceException("get user information failed");
		}

		return loadedUser;
	}
}
