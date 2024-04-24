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

package com.github.tangyi.auth.service;

import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.enums.LoginTypeEnum;
import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.model.UserToken;
import com.github.tangyi.common.security.TokenManager;
import com.github.tangyi.common.utils.RUtil;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserTokenService {

	private final TokenManager tokenManager;

	public void generateAndSaveToken(HttpServletRequest req, HttpServletResponse res, CustomUserDetails details) {
		this.generateAndSaveToken(req, res, details, true);
	}

	public Map<String, Object> generateAndSaveToken(HttpServletRequest req, HttpServletResponse res,
			CustomUserDetails details, boolean writeRes) {
		ImmutablePair<UserToken, Integer> pair = this.buildUserToken(req, details);
		UserToken userToken = pair.getLeft();
		LoginTypeEnum type = details.getLoginType();
		if (this.tokenManager.saveToken(userToken, pair.getRight())) {
			String token = this.tokenManager.createToken(userToken);
			Map<String, Object> map = Maps.newHashMapWithExpectedSize(3);
			map.put("token", token);
			map.put(CommonConstant.TENANT_CODE, details.getTenantCode());
			map.put("hasPhone", StringUtils.isNotEmpty(details.getPhone()));
			if (writeRes) {
				RUtil.out(res, R.success(map));
			}
			return map;
		} else {
			log.info("Login failed, id: {}, identify: {}, loginType: {}", details.getId(), details.getUsername(),
					type.getType());
			if (writeRes) {
				RUtil.out(res, R.error("Login failed."));
			}
		}
		return Collections.emptyMap();
	}

	private ImmutablePair<UserToken, Integer> buildUserToken(HttpServletRequest req, CustomUserDetails details) {
		// 是否"记住我"
		boolean remember = Boolean.parseBoolean(req.getParameter("remember"));
		LocalDateTime issuedAt = LocalDateTime.now();
		// 默认 240 分钟有效，"记住我" 7 天有效
		LocalDateTime expireAt = issuedAt.plusSeconds(remember ?
				TimeUnit.DAYS.toSeconds(TokenManager.TOKEN_REMEMBER_EXPIRE_DAY) :
				TimeUnit.MINUTES.toSeconds((TokenManager.TOKEN_EXPIRE_MINUTE)));
		String ip = req.getRemoteAddr();
		String userAgent = req.getHeader(HttpHeaders.USER_AGENT);
		UserToken token = new UserToken();
		token.setId(UUID.randomUUID().toString().replace("-", ""));
		token.setUserId(details.getId());
		token.setRole(this.parseUserRole(details));
		token.setIdentify(details.getUsername());
		token.setLoginType(details.getLoginType().getType());
		token.setTenantCode(details.getTenantCode());
		token.setIp(ip);
		token.setUserAgent(userAgent);
		token.setIssuedAt(issuedAt);
		token.setExpiresAt(expireAt);
		token.setRemember(remember);

		// 距离过期时间剩余的秒数
		int expireSeconds = (int) ChronoUnit.SECONDS.between(issuedAt, expireAt);
		return new ImmutablePair<>(token, expireSeconds);
	}

	private String parseUserRole(CustomUserDetails details) {
		String role = null;
		if (CollectionUtils.isNotEmpty(details.getAuthorities())) {
			role = details.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.joining(","));
		}
		return role;
	}
}
