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
		UserToken token = this.parseUserToken(req);
		if (token == null) {
			throw new IllegalStateException("Invalid token.");
		}
		Long uid = token.getUserId();
		if (this.tokenManager.tokenExist(uid)) {
			this.tokenManager.deleteToken(uid);
			log.info("Delete token finished, uid: {}", uid);
		}
		Collection<GrantedAuthority> authorities = Lists.newArrayList();
		if (StringUtils.isNotEmpty(token.getRole())) {
			for (String role : StringUtils.split(token.getRole(), CommonConstant.COMMA)) {
				authorities.add((GrantedAuthority) () -> role);
			}
		}
		CustomUserDetails d = new CustomUserDetails(uid, token.getIdentify(), authorities, token.getTenantCode());
		d.setLoginType(LoginTypeEnum.valueOf(token.getLoginType()));
		// TODO phone 需要回填
		//details.setPhone("");
		Map<String, Object> map = this.userTokenService.generateAndSaveToken(req, res, d, false);
		log.info("Refresh token finished, uid: {}", uid);
		return R.success(map);
	}

	@GetMapping("logout")
	public R<Boolean> logout(HttpServletRequest req) {
		UserToken token = this.parseUserToken(req);
		if (token != null && this.tokenManager.tokenExist(token.getUserId())) {
			this.tokenManager.deleteToken(token.getUserId());
		}
		return R.success(Boolean.TRUE);
	}

	private UserToken parseUserToken(HttpServletRequest req) {
		String authorization = req.getHeader(SecurityConstant.AUTHORIZATION);
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

		UserToken uToken = new UserToken();
		uToken.setId(id);
		uToken.setUserId(Long.parseLong(userId));
		uToken.setTenantCode(tenantCode);
		uToken.setIdentify(identify);
		uToken.setRole(role);
		uToken.setLoginType(loginType);
		return uToken;
	}
}
