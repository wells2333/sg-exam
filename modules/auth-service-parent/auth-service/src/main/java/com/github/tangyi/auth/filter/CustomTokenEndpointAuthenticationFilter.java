package com.github.tangyi.auth.filter;

import com.github.tangyi.auth.model.CustomUserDetails;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.constant.ServiceConstant;
import com.github.tangyi.common.core.model.Log;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.user.api.feign.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 登录成功后的处理，如记录登录日志
 *
 * @author tangyi
 * @date 2019-10-11 12:08
 */
@Slf4j
public class CustomTokenEndpointAuthenticationFilter extends TokenEndpointAuthenticationFilter {

	private UserServiceClient userServiceClient;

	public CustomTokenEndpointAuthenticationFilter(AuthenticationManager authenticationManager,
			OAuth2RequestFactory oAuth2RequestFactory, UserServiceClient userServiceClient) {
		super(authenticationManager, oAuth2RequestFactory);
		this.userServiceClient = userServiceClient;
	}

	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		// 登录成功后的处理
		log.info("CustomTokenEndpointAuthenticationFilter onSuccessfulAuthentication");
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String tenantCode = userDetails.getTenantCode();
		String username = userDetails.getUsername();
		log.info("CustomTokenEndpointAuthenticationFilter 登录成功, tenantCode: {}, username: {}", tenantCode, username);
		// 记录日志
		Log logInfo = new Log();
		logInfo.setCommonValue(username, SysUtil.getSysCode(), tenantCode);
		logInfo.setTitle("用户登录");
		logInfo.setType(CommonConstant.STATUS_NORMAL);
		logInfo.setMethod(request.getMethod());
		logInfo.setTime(String.valueOf(System.currentTimeMillis() - userDetails.getStart()));
		logInfo.setRequestUri(request.getRequestURI());
		// 获取ip、浏览器信息
		logInfo.setIp(request.getRemoteAddr());
		logInfo.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		logInfo.setServiceId(ServiceConstant.AUTH_SERVICE);
		// 记录日志
		saveLoginSuccessLog(logInfo);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// 认证前的特殊处理
		//		if (!condition) {
		//			throw new AuthenticationServiceException("condition not satisfied");
		//		}
		log.info("CustomTokenEndpointAuthenticationFilter doFilter");
		super.doFilter(req, res, chain);
	}

	/**
	 * 异步记录登录日志
	 *
	 * @author tangyi
	 * @date 2019/05/30 23:30
	 */
	@Async
	public void saveLoginSuccessLog(Log logInfo) {
		try {
			userServiceClient.saveLog(logInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
