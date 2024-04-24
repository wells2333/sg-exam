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

package com.github.tangyi.common.security;

import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.model.UserToken;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.ObjectUtil;
import com.google.common.collect.Maps;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class TokenManager {

	private static final String TOKEN_SIGN_KEY = EnvUtils.getValue("TOKEN_SIGN_KEY", "123456");

	public static final long TOKEN_EXPIRE_MINUTE = EnvUtils.getLong("TOKEN_EXPIRE_MINUTE", 240);
	public static final long TOKEN_AUTO_RESET_EXPIRE_MINUTE = EnvUtils.getLong("TOKEN_AUTO_RESET_EXPIRE_MINUTE", 10);
	public static final long TOKEN_REMEMBER_EXPIRE_DAY = EnvUtils.getLong("TOKEN_REMEMBER_EXPIRE_DAY", 7);
	public static final String ID = "id";
	public static final String ROLE_KEY = "role";
	public static final String IDENTIFY = "identify";
	public static final String USER_ID = "userId";
	public static final String LOGIN_TYPE = "loginType";
	public static final String TENANT_CODE = "tenantCode";
	public static final String TOKEN_KEY_PREFIX = "user_token:";

	private final RedisTemplate redisTemplate;

	public TokenManager(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public String createToken(UserToken userToken) {
		Map<String, String> body = Maps.newHashMapWithExpectedSize(6);
		body.put(TENANT_CODE, userToken.getTenantCode());
		body.put(ID, ObjectUtil.toString(userToken.getId()));
		body.put(USER_ID, ObjectUtil.toString(userToken.getUserId()));
		body.put(LOGIN_TYPE, userToken.getLoginType());
		body.put(ROLE_KEY, userToken.getRole());
		body.put(IDENTIFY, userToken.getIdentify());
		return this.createToken(userToken, body);
	}

	public String createToken(UserToken userToken, Map<String, String> body) {
		JwtBuilder builder = Jwts.builder().setSubject(userToken.getIdentify());
		if (MapUtils.isNotEmpty(body)) {
			body.forEach(builder::claim);
		}
		Date issuedAt = new Date(userToken.getIssuedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		builder.setIssuedAt(issuedAt)    //
				.setId(userToken.getId())    //
				.signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY);
		return SecurityConstant.BEARER + builder.compressWith(CompressionCodecs.GZIP).compact();
	}

	public boolean saveToken(UserToken userToken, int expireSeconds) {
		try {
			this.redisTemplate.opsForValue()
					.set(tokenKey(userToken.getUserId()), userToken, expireSeconds, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			log.error("Failed to save token", e);
		}
		return false;
	}

	public UserToken getToken(String userId) {
		return (UserToken) this.redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + userId);
	}

	public void updateExpireSeconds(UserToken userToken, int expireSeconds) {
		this.redisTemplate.expire(tokenKey(userToken.getUserId()), expireSeconds, TimeUnit.SECONDS);
		log.info("Execute expire token finished, userId: {}, expireSeconds: {}", userToken.getUserId(), expireSeconds);
	}

	@SuppressWarnings("unchecked")
	public Boolean tokenExist(Long userId) {
		return this.redisTemplate.hasKey(tokenKey(userId));
	}

	public void deleteToken(Long userId) {
		this.redisTemplate.delete(tokenKey(userId));
		log.info("Execute delete token finished, userId: {}", userId);
	}

	private String tokenKey(Long userId) {
		// key: user_token:${userId}
		return TOKEN_KEY_PREFIX + userId;
	}

	public Claims getTokenBody(String token) {
		return Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token).getBody();
	}

	public String parseRole(String token) {
		return getTokenBody(token).get(ROLE_KEY).toString();
	}

	public Long parseUserId(String token) {
		return Long.valueOf(getTokenBody(token).get(USER_ID).toString());
	}

	public void refreshToken(Long id) {
		this.redisTemplate.opsForValue().set(TOKEN_KEY_PREFIX + id, null, System.currentTimeMillis() + 1000L * 60);
		log.info("Execute refresh token finished, id: {}", id);
	}
}
