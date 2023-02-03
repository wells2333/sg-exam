package com.github.tangyi.common.security;

import com.beust.jcommander.internal.Maps;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.model.UserToken;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.ObjectUtil;
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

	public static final long TOKEN_EXPIRE = Long.parseLong(EnvUtils.getValue("TOKEN_EXPIRE", 240 + ""));

	public static final long TOKEN_REMEMBER_EXPIRE = Long.parseLong(
			EnvUtils.getValue("TOKEN_REMEMBER_EXPIRE_SECONDS", "7"));

	public static final String TOKEN_SIGN_KEY = EnvUtils.getValue("TOKEN_SIGN_KEY", "123456");

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
		Map<String, String> body = Maps.newHashMap();
		body.put(TENANT_CODE, userToken.getTenantCode());
		body.put(ID, ObjectUtil.toString(userToken.getId()));
		body.put(USER_ID, ObjectUtil.toString(userToken.getUserId()));
		body.put(LOGIN_TYPE, userToken.getLoginType());
		body.put(ROLE_KEY, userToken.getRole());
		body.put(IDENTIFY, userToken.getIdentify());
		return createToken(userToken, body);
	}

	public String createToken(UserToken userToken, Map<String, String> body) {
		JwtBuilder builder = Jwts.builder().setSubject(userToken.getIdentify());
		if (MapUtils.isNotEmpty(body)) {
			body.forEach(builder::claim);
		}
		builder.setIssuedAt(new Date(userToken.getIssuedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
				.setId(userToken.getId()).signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY);
		return SecurityConstant.BEARER + builder.compressWith(CompressionCodecs.GZIP).compact();
	}

	public boolean saveToken(UserToken userToken, int expire) {
		try {
			redisTemplate.opsForValue().set(tokenKey(userToken.getUserId()), userToken, expire, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			log.error("saveToken failed", e);
		}
		return false;
	}

	public UserToken getToken(String userId) {
		return (UserToken) redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + userId);
	}

	public void expireToken(UserToken userToken, int expire) {
		redisTemplate.expire(tokenKey(userToken.getUserId()), expire, TimeUnit.SECONDS);
	}

	@SuppressWarnings("unchecked")
	public Boolean tokenExist(Long userId) {
		return redisTemplate.hasKey(tokenKey(userId));
	}

	public void deleteToken(Long userId) {
		redisTemplate.delete(tokenKey(userId));
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
		redisTemplate.opsForValue().set(TOKEN_KEY_PREFIX + id, null, System.currentTimeMillis() + 1000L * 60);
	}
}
