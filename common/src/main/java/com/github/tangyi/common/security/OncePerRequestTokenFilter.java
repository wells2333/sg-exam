package com.github.tangyi.common.security;

import com.beust.jcommander.internal.Lists;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.ApiMsg;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.model.UserToken;
import com.github.tangyi.common.properties.FilterIgnorePropertiesConfig;
import com.github.tangyi.common.security.exceptions.TokenExpireException;
import com.github.tangyi.common.utils.ObjectUtil;
import com.github.tangyi.common.utils.RUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OncePerRequestTokenFilter extends OncePerRequestFilter {

	private final TokenManager tokenManager;

	private final List<AntPathRequestMatcher> matchers = Lists.newArrayList();

	public OncePerRequestTokenFilter(TokenManager tokenManager,
			FilterIgnorePropertiesConfig filterIgnorePropertiesConfig) {
		this.tokenManager = tokenManager;
		List<String> urls = filterIgnorePropertiesConfig.getUrls();
		if (CollectionUtils.isNotEmpty(urls)) {
			for (String url : urls) {
				matchers.add(new AntPathRequestMatcher(url));
			}
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (!isFilterUrl(request)) {
			try {
				parseToken(request);
				filterChain.doFilter(request, response);
			} catch (CommonException e) {
				RUtil.out(response, R.error(ApiMsg.KEY_TOKEN, e.getMessage()));
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}

	private void parseToken(HttpServletRequest request) {
		String authorization = request.getHeader(SecurityConstant.AUTHORIZATION);
		if (StringUtils.isEmpty(authorization)) {
			authorization = request.getParameter(SecurityConstant.AUTHORIZATION);
		}
		SgPreconditions.checkBoolean(StringUtils.isEmpty(authorization), "没有用户凭证");
		SgPreconditions.checkBoolean(!authorization.startsWith(SecurityConstant.BEARER), "token非法");
		String token = authorization.substring(SecurityConstant.BEARER.length());
		Claims claims = parseTokenBody(token);
		String identify = ObjectUtil.toString(claims.get(TokenManager.IDENTIFY));
		String userId = ObjectUtil.toString(claims.get(TokenManager.USER_ID));
		String id = ObjectUtil.toString(claims.get(TokenManager.ID));
		String tenantCode = ObjectUtil.toString(claims.get(TokenManager.TENANT_CODE));
		if (StringUtils.isNotEmpty(identify) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserToken userToken = tokenManager.getToken(userId, id);
			if (userToken == null || !userToken.getUserId().toString().equals(userId) || !userToken.getId()
					.equals(id)) {
				throw new TokenExpireException("token失效，请重新登录");
			}
			// 默认续期30分钟，"记住我"续期7天
			long expire = userToken.isRemember() ?
					TimeUnit.DAYS.toSeconds(TokenManager.TOKEN_REMEMBER_EXPIRE) :
					TimeUnit.MINUTES.toSeconds(TokenManager.TOKEN_EXPIRE);
			// token接近过期则自动续期
			Duration duration = Duration.between(LocalDateTime.now(), userToken.getExpiresAt());
			if (duration.toMinutes() < TokenManager.TOKEN_EXPIRE) {
				tokenManager.expireToken(userToken, (int) expire);
			}
			String role = userToken.getRole();
			Collection<GrantedAuthority> authorities = Lists.newArrayList();
			if (StringUtils.isNotEmpty(role)) {
				for (String r : role.split(CommonConstant.COMMA)) {
					authorities.add((GrantedAuthority) () -> r);
				}
			}
			CustomUserDetails details = new CustomUserDetails(Long.valueOf(userId), identify, "", authorities,
					tenantCode);
			JwtAuthenticationToken authentication = new JwtAuthenticationToken(details, authorities);
			authentication.setAuthenticated(true);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	}

	private Claims parseTokenBody(String token) {
		Claims claims;
		try {
			claims = tokenManager.getTokenBody(token);
		} catch (Exception e) {
			log.error("parseTokenBody failed", e);
			throw new TokenExpireException("parseTokenBody failed");
		}
		return claims;
	}

	private boolean isFilterUrl(HttpServletRequest request) {
		boolean match = false;
		for (AntPathRequestMatcher matcher : matchers) {
			if (matcher.matcher(request).isMatch()) {
				match = true;
				break;
			}
		}
		return match;
	}
}
