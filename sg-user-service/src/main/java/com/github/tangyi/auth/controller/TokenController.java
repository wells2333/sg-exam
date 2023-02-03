package com.github.tangyi.auth.controller;

import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.model.UserToken;
import com.github.tangyi.common.security.TokenManager;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/v1/token")
@AllArgsConstructor
public class TokenController extends BaseController {

	private final TokenManager tokenManager;

	@GetMapping("validToken")
	public R<Boolean> validToken(HttpServletRequest request) {
		UserToken userToken = parseUserToken(request);
		return R.success(userToken != null && tokenManager.tokenExist(userToken.getUserId()));
	}

	@GetMapping("logout")
	public R<Boolean> logout(HttpServletRequest request) {
		UserToken userToken = parseUserToken(request);
		if (userToken != null && tokenManager.tokenExist(userToken.getUserId())) {
			tokenManager.deleteToken(userToken.getUserId());
		}
		return R.success(Boolean.TRUE);
	}

	private UserToken parseUserToken(HttpServletRequest request) {
		String authorization = request.getHeader(SecurityConstant.AUTHORIZATION);
		if (StringUtils.isEmpty(authorization)) {
			return null;
		}
		String token = authorization.substring(SecurityConstant.BEARER.length());
		Claims claims = tokenManager.getTokenBody(token);
		UserToken userToken = new UserToken();
		Long userId = Long.parseLong(claims.get(TokenManager.USER_ID) + "");
		userToken.setUserId(userId);
		String id = claims.get(TokenManager.ID) + "";
		userToken.setId(id);
		return userToken;
	}
}
