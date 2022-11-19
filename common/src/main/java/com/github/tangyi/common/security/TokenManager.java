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

/**
 *
 * @author tangyi
 * @date 2022/5/1 2:27 下午
 */
@Slf4j
@Component
public class TokenManager {

	public static final long TOKEN_EXPIRE = Long.parseLong(EnvUtils.getValue("TOKEN_EXPIRE", 180 + ""));

	public static final long TOKEN_REMEMBER_EXPIRE = Long.parseLong(
			EnvUtils.getValue("TOKEN_REMEMBER_EXPIRE_SECONDS", "7"));

	public static final String TOKEN_SIGN_KEY = EnvUtils.getValue("TOKEN_SIGN_KEY", "123456");

	public static final String ID = "id";

	public static final String ROLE_KEY = "role";

	public static final String IDENTIFY = "identify";

	public static final String USER_ID = "userId";

	public static final String LOGIN_TYPE = "loginType";

	public static final String TENANT_CODE = "tenantCode";

	public static final String TOKEN_KEY_PREFIX = "user_token_";

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
		builder
				// token的过期时间由Redis维护
				.setIssuedAt(
						new Date(userToken.getIssuedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
				// 使用UUID作为token的ID
				.setId(userToken.getId()).signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY);
		return SecurityConstant.BEARER + builder.compressWith(CompressionCodecs.GZIP).compact();
	}

	/**
	 * 保存token到Redis
	 * key: user_token_${userId}_${tokenId}
	 * @param userToken userToken
	 * @param expire expire
	 */
	@SuppressWarnings("unchecked")
	public boolean saveToken(UserToken userToken, int expire) {
		try {
			redisTemplate.opsForValue().set(tokenKey(userToken), userToken, expire, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			log.error("saveToken failed", e);
		}
		return false;
	}

	/**
	 * 从Redis获取token信息
	 * @param userId userId
	 * @param id id
	 * @return UserToken
	 */
	public UserToken getToken(String userId, String id) {
		return (UserToken) redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + userId + "_" + id);
	}

	/**
	 * 设置超时时间
	 * @param userToken userToken
	 * @param expire expire
	 */
	@SuppressWarnings("unchecked")
	public void expireToken(UserToken userToken, int expire) {
		redisTemplate.expire(tokenKey(userToken), expire, TimeUnit.SECONDS);
	}

	/**
	 * token 是否存在
	 * @param userToken userToken
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean tokenExist(UserToken userToken) {
		return redisTemplate.hasKey(tokenKey(userToken));
	}

	/**
	 * 删除token
	 * @param userToken userToken
	 */
	@SuppressWarnings("unchecked")
	public void deleteToken(UserToken userToken) {
		redisTemplate.delete(tokenKey(userToken));
	}

	private String tokenKey(UserToken userToken) {
		return TOKEN_KEY_PREFIX + userToken.getUserId() + "_" + userToken.getId();
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
