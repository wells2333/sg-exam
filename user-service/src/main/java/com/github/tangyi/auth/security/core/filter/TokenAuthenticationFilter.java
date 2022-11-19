package com.github.tangyi.auth.security.core.filter;

import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.security.TokenManager;
import com.github.tangyi.common.utils.ObjectUtil;
import com.github.tangyi.common.utils.RUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 授权
 * @author tangyi
 * @date 2022/5/1 3:40 下午
 */
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

	private final TokenManager tokenManager;

	public TokenAuthenticationFilter(AuthenticationManager authenticationManager, TokenManager tokenManager) {
		super(authenticationManager);
		this.tokenManager = tokenManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!AnyRequestMatcher.INSTANCE.matcher(request).isMatch()) {
			UsernamePasswordAuthenticationToken authentication = null;
			try {
				authentication = getAuthentication(request);
			} catch (Exception e) {
				RUtil.out(response, R.success(e.getMessage()));
			}

			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				chain.doFilter(request, response);
			} else {
				RUtil.out(response, R.error("authentication is null"));
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * 根据Authorization header，解析出token里面的信息
	 * @param request request
	 * @return UsernamePasswordAuthenticationToken
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstant.AUTHORIZATION);
		if (StringUtils.isEmpty(token)) {
			token = request.getParameter(SecurityConstant.AUTHORIZATION);
		}
		if (StringUtils.isNotEmpty(token)) {
			token = token.substring(SecurityConstant.BEARER.length());
			Claims claims = tokenManager.getTokenBody(token);
			// 解析出identify
			String identify = ObjectUtil.toString(claims.get(TokenManager.IDENTIFY));
			if (StringUtils.isNotEmpty(identify)) {
				String role = ObjectUtil.toString(claims.get(TokenManager.ROLE_KEY));
				List<SimpleGrantedAuthority> authorities = Stream.of(role.split(CommonConstant.COMMA))
						.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
				return new UsernamePasswordAuthenticationToken(identify, token, authorities);
			}
		}
		return null;
	}
}
