package com.github.tangyi.auth.controller;

import com.beust.jcommander.internal.Lists;
import com.github.tangyi.auth.service.UserTokenService;
import com.github.tangyi.common.base.BaseController;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.enums.LoginTypeEnum;
import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.model.UserToken;
import com.github.tangyi.common.security.TokenManager;
import com.github.tangyi.common.utils.ObjectUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/token")
@AllArgsConstructor
public class TokenController extends BaseController {

	private final TokenManager tokenManager;

	private final UserTokenService userTokenService;

	@GetMapping("validToken")
	public R<Boolean> validToken(HttpServletRequest req) {
		UserToken userToken = this.parseUserToken(req);
		return R.success(userToken != null && this.tokenManager.tokenExist(userToken.getUserId()));
	}

	@GetMapping("refreshToken")
	public R<Map<String, Object>> refreshToken(HttpServletRequest req, HttpServletResponse res) {
		UserToken userToken = this.parseUserToken(req);
		if (userToken == null) {
			throw new IllegalStateException("Invalid token.");
		}
		Long userId = userToken.getUserId();
		if (this.tokenManager.tokenExist(userId)) {
			this.tokenManager.deleteToken(userId);
			log.info("Delete token finished, userId: {}", userId);
		}
		Collection<GrantedAuthority> authorities = Lists.newArrayList();
		if (StringUtils.isNotEmpty(userToken.getRole())) {
			for (String role : StringUtils.split(userToken.getRole(), CommonConstant.COMMA)) {
				authorities.add((GrantedAuthority) () -> role);
			}
		}
		CustomUserDetails details = new CustomUserDetails(userId, userToken.getIdentify(), authorities,
				userToken.getTenantCode());
		details.setLoginType(LoginTypeEnum.valueOf(userToken.getLoginType()));
		// TODO phone 需要回填
		//details.setPhone("");
		Map<String, Object> map = this.userTokenService.generateAndSaveToken(req, res, details, false);
		log.info("Refresh token finished, userId: {}", userId);
		return R.success(map);
	}

	@GetMapping("logout")
	public R<Boolean> logout(HttpServletRequest request) {
		UserToken userToken = this.parseUserToken(request);
		if (userToken != null && this.tokenManager.tokenExist(userToken.getUserId())) {
			this.tokenManager.deleteToken(userToken.getUserId());
		}
		return R.success(Boolean.TRUE);
	}

	private UserToken parseUserToken(HttpServletRequest request) {
		String authorization = request.getHeader(SecurityConstant.AUTHORIZATION);
		if (StringUtils.isEmpty(authorization)) {
			return null;
		}
		String token = authorization.substring(SecurityConstant.BEARER.length());
		Claims claims = this.tokenManager.getTokenBody(token);
		String id = ObjectUtil.toString(claims.get(TokenManager.ID));
		String userId = ObjectUtil.toString(claims.get(TokenManager.USER_ID));
		String tenantCode = ObjectUtil.toString(claims.get(TokenManager.TENANT_CODE));
		String identify = ObjectUtil.toString(claims.get(TokenManager.IDENTIFY));
		String role = ObjectUtil.toString(claims.get(TokenManager.ROLE_KEY));
		String loginType = ObjectUtil.toString(claims.get(TokenManager.LOGIN_TYPE));

		UserToken userToken = new UserToken();
		userToken.setId(id);
		userToken.setUserId(Long.parseLong(userId));
		userToken.setTenantCode(tenantCode);
		userToken.setIdentify(identify);
		userToken.setRole(role);
		userToken.setLoginType(loginType);
		return userToken;
	}
}
