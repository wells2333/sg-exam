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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.auth.service.UserTokenService;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.CustomUserDetails;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class WxLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final String SPRING_SECURITY_FORM_WX_KEY = "code";

	@Getter
	@Setter
	private String wxParameter = SPRING_SECURITY_FORM_WX_KEY;

	@Getter
	@Setter
	private boolean postOnly = true;

	@Getter
	@Setter
	private AuthenticationEventPublisher eventPublisher;

	@Getter
	@Setter
	private AuthenticationEntryPoint authenticationEntryPoint;

	private final UserTokenService userTokenService;

	public WxLoginFilter(UserTokenService userTokenService) {
		super(new AntPathRequestMatcher(SecurityConstant.WX_LOGIN_URL, HttpMethod.POST.name()));
		this.userTokenService = userTokenService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String code = StringUtils.defaultIfEmpty(obtainCode(request), "").trim();
		WxAuthenticationToken token = new WxAuthenticationToken(request.getHeader(CommonConstant.TENANT_CODE_HEADER),
				code);
		setWxUserDetails(request, token);
		setDetails(request, token);
		Authentication auth = null;
		try {
			auth = this.getAuthenticationManager().authenticate(token);
			log.info("Wx authentication successfully, code: {}, principal: {}", code, auth.getPrincipal());
			eventPublisher.publishAuthenticationSuccess(auth);
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			log.error("Failed to authentication wx login request, code: {}", code, e);
			eventPublisher.publishAuthenticationFailure(new BadCredentialsException(e.getMessage(), e),
					new PreAuthenticatedAuthenticationToken("access-token", "N/A"));
			try {
				authenticationEntryPoint.commence(request, response, new UsernameNotFoundException(e.getMessage(), e));
			} catch (Exception ex) {
				log.error("wx authenticationEntryPoint handle failed, code: {}", code, ex);
			}
		}
		return auth;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) {
		CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
		userTokenService.generateAndSaveToken(req, res, details);
	}

	private String obtainCode(HttpServletRequest request) {
		return request.getParameter(wxParameter);
	}

	private void setDetails(HttpServletRequest request, WxAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	private void setWxUserDetails(HttpServletRequest request, WxAuthenticationToken authRequest) {
		ObjectMapper mapper = new ObjectMapper();
		try (InputStream in = request.getInputStream()) {
			authRequest.setWxUser(mapper.readValue(in, WxUser.class));
		} catch (IOException e) {
			log.error("init mobile userDetails failed", e);
		}
	}
}
