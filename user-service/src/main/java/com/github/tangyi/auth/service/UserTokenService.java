package com.github.tangyi.auth.service;

import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.model.UserToken;
import com.github.tangyi.common.security.TokenManager;
import com.github.tangyi.common.utils.RUtil;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author tangyi
 * @date 2022/5/2 10:04 上午
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserTokenService {

	private final TokenManager tokenManager;

	public Map<String, Object> generateAndSaveToken(HttpServletRequest req, HttpServletResponse res,
			CustomUserDetails details, boolean writeRes) {
		String role = null;
		if (CollectionUtils.isNotEmpty(details.getAuthorities())) {
			role = details.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.joining(","));
		}
		// 是否"记住我"
		boolean remember = Boolean.parseBoolean(req.getParameter("remember"));
		String ip = req.getRemoteAddr();
		String userAgent = req.getHeader(HttpHeaders.USER_AGENT);
		LocalDateTime issuedAt = LocalDateTime.now();
		// 默认180分钟有效，"记住我"7天有效
		LocalDateTime expireAt = issuedAt.plusSeconds(remember ?
				TimeUnit.DAYS.toSeconds(TokenManager.TOKEN_REMEMBER_EXPIRE) :
				TimeUnit.MINUTES.toSeconds((TokenManager.TOKEN_EXPIRE)));
		UserToken userToken = new UserToken();
		userToken.setId(UUID.randomUUID().toString().replace("-", ""));
		userToken.setUserId(details.getId());
		userToken.setRole(role);
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
		// 保存到Redis
		if (tokenManager.saveToken(userToken, expireSeconds)) {
			// 生成token
			String token = tokenManager.createToken(userToken);
			Map<String, Object> map = Maps.newHashMapWithExpectedSize(3);
			map.put("token", token);
			map.put(CommonConstant.TENANT_CODE, details.getTenantCode());
			log.info("login success, identify: {}, loginType: {}", details.getUsername(),
					details.getLoginType().getType());
			if (writeRes) {
				RUtil.out(res, R.success(map));
			}
			return map;
		} else {
			log.info("login failed, identify: {}, loginType: {}", details.getUsername(),
					details.getLoginType().getType());
			if (writeRes) {
				RUtil.out(res, R.error("login failed"));
			}
		}
		return null;
	}

	/**
	 * 生成token，并保存到Redis
	 * @param req req
	 * @param res res
	 * @param details details
	 */
	public Map<String, Object> generateAndSaveToken(HttpServletRequest req, HttpServletResponse res,
			CustomUserDetails details) {
		return generateAndSaveToken(req, res, details, true);
	}
}
