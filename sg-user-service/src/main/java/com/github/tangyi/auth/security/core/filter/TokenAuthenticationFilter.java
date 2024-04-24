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

package com.github.tangyi.auth.security.core.filter;

import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.security.TokenManager;
import com.github.tangyi.common.utils.ObjectUtil;
import com.github.tangyi.common.utils.RUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

	private final TokenManager tokenManager;

	public TokenAuthenticationFilter(AuthenticationManager authenticationManager, TokenManager tokenManager) {
		super(authenticationManager);
		this.tokenManager = tokenManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// 本代码不执行
		if (!AnyRequestMatcher.INSTANCE.matcher(req).isMatch()) {
			UsernamePasswordAuthenticationToken authentication = null;
			try {
				authentication = getAuthentication(req);
			} catch (Exception e) {
				RUtil.out(res, R.success(e.getMessage()));
			}

			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				chain.doFilter(req, res);
			} else {
				RUtil.out(res, R.error("authentication is null"));
			}
		} else {
			chain.doFilter(req, res);
		}
	}

	/**
	 * 根据 Authorization header，解析出 token 里面的信息
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		String token = req.getHeader(SecurityConstant.AUTHORIZATION);
		if (StringUtils.isEmpty(token)) {
			token = req.getParameter(SecurityConstant.AUTHORIZATION);
		}
		if (StringUtils.isNotEmpty(token)) {
			token = token.substring(SecurityConstant.BEARER.length());
			Claims claims = tokenManager.getTokenBody(token);
			String identify = ObjectUtil.toString(claims.get(TokenManager.IDENTIFY));
			if (StringUtils.isNotEmpty(identify)) {
				String role = ObjectUtil.toString(claims.get(TokenManager.ROLE_KEY));
				List<SimpleGrantedAuthority> authorities = Stream.of(role.split(CommonConstant.COMMA))
						.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
				return new UsernamePasswordAuthenticationToken(identify, token, authorities);
			}
		}
		return null;
	}
}
