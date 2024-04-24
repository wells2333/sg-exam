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

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.auth.security.core.event.CustomAuthenticationSuccessEvent;
import com.github.tangyi.auth.service.UserTokenService;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.AesUtil;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.RUtil;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

	private static final String PASSWORD = "password";
	private static final String CREDENTIAL = "credential";
	public static String[] ACCESS_TOKEN_URLS;

	static {
		String urls = EnvUtils.getValue("ACCESS_TOKEN_LOGIN_URLS");
		if (StringUtils.isEmpty(urls)) {
			String[] arr = {SecurityConstant.LOGIN_URL, SecurityConstant.REGISTER, SecurityConstant.MOBILE_LOGIN_URL};
			urls = StringUtils.join(arr, CommonConstant.COMMA);
		}
		ACCESS_TOKEN_URLS = StringUtils.split(urls, CommonConstant.COMMA);
	}

	private final AuthenticationManager authenticationManager;
	private final UserTokenService userTokenService;
	private final SysProperties sysProperties;

	public TokenLoginFilter(AuthenticationManager authenticationManager, UserTokenService userTokenService,
			SysProperties sysProperties) {
		this.authenticationManager = authenticationManager;
		this.userTokenService = userTokenService;
		this.sysProperties = sysProperties;
		this.setPostOnly(false);
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		Map<String, String[]> map = req.getParameterMap();
		String username = map.get("username")[0];
		String credential = null;
		if (map.containsKey(CREDENTIAL)) {
			credential = map.get(CREDENTIAL)[0];
		}
		String password = null;
		if (map.containsKey(PASSWORD)) {
			password = map.get(PASSWORD)[0];
		}
		String decryptResult = decryptPassword(req, password, credential);
		return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, decryptResult, Lists.newArrayList()));
	}

	private String decryptPassword(HttpServletRequest req, String password, String credential) {
		String uri = req.getRequestURI();
		if (HttpMethod.POST.matches(req.getMethod()) && StrUtil.containsAnyIgnoreCase(uri, ACCESS_TOKEN_URLS)
				&& needDecrypt(req)) {
			if (StringUtils.isNotBlank(credential)) {
				password = doDecrypt(credential);
			} else if (StringUtils.isNotBlank(password)) {
				password = doDecrypt(password);
			}
		}
		return password;
	}

	/**
	 * 授权类型为密码模式则解密
	 */
	private boolean needDecrypt(HttpServletRequest req) {
		String uri = req.getRequestURI();
		String grantType = req.getParameter(SecurityConstant.GRANT_TYPE);
		return CommonConstant.GRANT_TYPE_PASSWORD.equals(grantType) || StrUtil.containsAnyIgnoreCase(uri,
				SecurityConstant.REGISTER);
	}

	private String doDecrypt(String target) {
		try {
			target = target.replaceAll("%2B", "\\+");
			target = AesUtil.decryptAES(target, sysProperties.getKey()).trim();
		} catch (Exception e) {
			log.error("Failed to decrypt credential., credential: {}", target, e);
		}
		return target;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) {
		CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
		userTokenService.generateAndSaveToken(req, res, details);
		SpringContextHolder.publishEvent(new CustomAuthenticationSuccessEvent(auth, details));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
			AuthenticationException failed) {
		if (failed instanceof BadCredentialsException) {
			log.error("Unsuccessful authentication: {}", failed.getMessage());
		} else {
			log.error("Unsuccessful authentication", failed);
		}
		RUtil.out(res, R.error(failed.getMessage()));
	}
}
