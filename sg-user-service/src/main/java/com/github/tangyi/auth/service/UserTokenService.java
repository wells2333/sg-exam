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
		LoginTypeEnum loginType = details.getLoginType();
		if (this.tokenManager.saveToken(userToken, pair.getRight())) {
			String token = this.tokenManager.createToken(userToken);
			Map<String, Object> map = Maps.newHashMapWithExpectedSize(3);
			map.put("token", token);
			map.put(CommonConstant.TENANT_CODE, details.getTenantCode());
			map.put("hasPhone", StringUtils.isNotEmpty(details.getPhone()));
			log.info("Login successfully, id: {}, identify: {}, loginType: {}", details.getId(), details.getUsername(),
					loginType.getType());
			if (writeRes) {
				RUtil.out(res, R.success(map));
			}
			return map;
		} else {
			log.info("Login failed, id: {}, identify: {}, loginType: {}", details.getId(), details.getUsername(),
					loginType.getType());
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
		UserToken userToken = new UserToken();
		userToken.setId(UUID.randomUUID().toString().replace("-", ""));
		userToken.setUserId(details.getId());
		userToken.setRole(this.parseUserRole(details));
		userToken.setIdentify(details.getUsername());
		userToken.setLoginType(details.getLoginType().getType());
		userToken.setTenantCode(details.getTenantCode());
		userToken.setIp(ip);
		userToken.setUserAgent(userAgent);
		userToken.setIssuedAt(issuedAt);
		userToken.setExpiresAt(expireAt);
		userToken.setRemember(remember);

		// 距离过期时间剩余的秒数
		int expireSeconds = (int) ChronoUnit.SECONDS.between(issuedAt, expireAt);
		return new ImmutablePair<>(userToken, expireSeconds);
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
