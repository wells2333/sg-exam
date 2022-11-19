package com.github.tangyi.auth.security.wx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.auth.service.UserTokenService;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.CustomUserDetails;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 微信登录filter，登录时支持传入用户的资料，如姓名、电话、性别等
 *
 * @author tangyi
 * @date 2019/07/05 19:32
 */
@Slf4j
public class WxLoginFilter extends AbstractAuthenticationProcessingFilter {

	/**
	 * 微信登录的code参数，后续用code换取openId和sessionKey
	 */
	private static final String SPRING_SECURITY_FORM_WX_KEY = "code";

	@Getter
	@Setter
	private String wxParameter = SPRING_SECURITY_FORM_WX_KEY;

	@Getter
	@Setter
	private boolean postOnly = true;

	@Getter
	@Setter
	private AuthenticationEventPublisher eventPublisher;

	@Getter
	@Setter
	private AuthenticationEntryPoint authenticationEntryPoint;

	private final UserTokenService userTokenService;

	public WxLoginFilter(UserTokenService userTokenService) {
		super(new AntPathRequestMatcher(SecurityConstant.WX_LOGIN_URL, HttpMethod.POST.name()));
		this.userTokenService = userTokenService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		// 获取code
		String code = StringUtils.defaultIfEmpty(obtainCode(request), "").trim();
		// 封装成token
		WxAuthenticationToken wxAuthenticationToken = new WxAuthenticationToken(
				request.getHeader(CommonConstant.TENANT_CODE_HEADER), code);
		// 封装其它基本信息
		setWxUserDetails(request, wxAuthenticationToken);
		setDetails(request, wxAuthenticationToken);
		Authentication auth = null;
		try {
			// 认证
			auth = this.getAuthenticationManager().authenticate(wxAuthenticationToken);
			log.info("wx authentication success, code: {}, principal: {}", code, auth.getPrincipal());
			// 认证成功
			eventPublisher.publishAuthenticationSuccess(auth);
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (Exception failed) {
			SecurityContextHolder.clearContext();
			log.error("wx authentication request failed, code: {}", code, failed);
			eventPublisher.publishAuthenticationFailure(new BadCredentialsException(failed.getMessage(), failed),
					new PreAuthenticatedAuthenticationToken("access-token", "N/A"));
			try {
				authenticationEntryPoint.commence(request, response,
						new UsernameNotFoundException(failed.getMessage(), failed));
			} catch (Exception e) {
				log.error("wx authenticationEntryPoint handle failed, code: {}", code, failed);
			}
		}
		return auth;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
		// 认证成功，生成token
		userTokenService.generateAndSaveToken(req, res, details);
	}

	private String obtainCode(HttpServletRequest request) {
		return request.getParameter(wxParameter);
	}

	private void setDetails(HttpServletRequest request, WxAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	/**
	 * 设置姓名、性别、头像等信息
	 *
	 * @param request     request
	 * @param authRequest authRequest
	 */
	private void setWxUserDetails(HttpServletRequest request, WxAuthenticationToken authRequest) {
		ObjectMapper mapper = new ObjectMapper();
		try (InputStream in = request.getInputStream()) {
			authRequest.setWxUser(mapper.readValue(in, WxUser.class));
		} catch (IOException e) {
			log.error("init mobile userDetails failed", e);
		}
	}
}
