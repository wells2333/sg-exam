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

import com.github.tangyi.auth.security.core.event.CustomAuthenticationFailureEvent;
import com.github.tangyi.auth.security.core.event.CustomAuthenticationSuccessEvent;
import com.github.tangyi.auth.security.core.user.CustomUserDetailsService;
import com.github.tangyi.common.utils.SpringContextHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@Data
public class WxAuthenticationProvider implements AuthenticationProvider {

	private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	private CustomUserDetailsService customUserDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		WxAuthenticationToken token = (WxAuthenticationToken) authentication;
		String principal = token.getPrincipal().toString();
		UserDetails details = customUserDetailsService.loadUserByWxCodeAndTenantCode(token.getTenantCode(), principal,
				token.getWxUser());
		if (details == null) {
			log.error("Failed to authentication : no credentials provided, principal: {}", principal);
			SpringContextHolder.publishEvent(new CustomAuthenticationFailureEvent(authentication, details));
			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.noopBindAccount",
							"Noop Bind Account"));
		}

		WxAuthenticationToken result = new WxAuthenticationToken(token.getTenantCode(), details,
				details.getAuthorities());
		result.setDetails(token.getDetails());
		SpringContextHolder.publishEvent(new CustomAuthenticationSuccessEvent(authentication, details));
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return WxAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
