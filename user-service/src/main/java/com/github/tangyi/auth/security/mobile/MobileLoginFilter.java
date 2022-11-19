package com.github.tangyi.auth.security.mobile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.auth.service.UserTokenService;
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
 * 手机登录filter
 *
 * @author tangyi
 * @date 2019/6/22 21:15
 */
@Slf4j
public class MobileLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";

	@Getter
	@Setter
	private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;

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

	public MobileLoginFilter(UserTokenService userTokenService) {
		super(new AntPathRequestMatcher(SecurityConstant.MOBILE_LOGIN_URL, HttpMethod.POST.name()));
		this.userTokenService = userTokenService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		// 获取手机登录的参数
		String mobile = StringUtils.defaultIfEmpty(obtainMobile(request), "").trim();
		// 封装成token
		MobileAuthenticationToken mobileAuthenticationToken = new MobileAuthenticationToken(mobile);
		// 封装其它基本信息
		setMobileUserDetails(request, mobileAuthenticationToken);
		setDetails(request, mobileAuthenticationToken);
		Authentication authResult = null;
		try {
			// 认证
			authResult = this.getAuthenticationManager().authenticate(mobileAuthenticationToken);
			log.info("mobile authentication success, mobile: {}, result: {}", mobile, authResult);
			// 认证成功
			eventPublisher.publishAuthenticationSuccess(authResult);
			SecurityContextHolder.getContext().setAuthentication(authResult);
		} catch (Exception failed) {
			SecurityContextHolder.clearContext();
			log.error("mobile authentication request failed, mobile: {}", mobile, failed);
			eventPublisher.publishAuthenticationFailure(new BadCredentialsException(failed.getMessage(), failed),
					new PreAuthenticatedAuthenticationToken("access-token", "N/A"));
			try {
				authenticationEntryPoint.commence(request, response,
						new UsernameNotFoundException(failed.getMessage(), failed));
			} catch (Exception e) {
				log.error("mobile authenticationEntryPoint handle failed, mobile: {}", mobile, failed);
			}
		}
		return authResult;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
		// 认证成功，生成token
		userTokenService.generateAndSaveToken(req, res, details);
	}

	private String obtainMobile(HttpServletRequest request) {
		return request.getParameter(mobileParameter);
	}

	private void setDetails(HttpServletRequest request, MobileAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	/**
	 * 设置姓名、性别、头像等信息
	 *
	 * @param request     request
	 * @param authRequest authRequest
	 */
	private void setMobileUserDetails(HttpServletRequest request, MobileAuthenticationToken authRequest) {
		ObjectMapper mapper = new ObjectMapper();
		try (InputStream in = request.getInputStream()) {
			authRequest.setMobileUser(mapper.readValue(in, MobileUser.class));
		} catch (IOException e) {
			log.error("init mobile userDetails failed", e);
		}
	}
}
