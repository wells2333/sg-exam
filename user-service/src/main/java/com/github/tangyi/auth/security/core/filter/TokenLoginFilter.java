package com.github.tangyi.auth.security.core.filter;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.auth.service.UserTokenService;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.AesUtil;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.RUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 认证
 * @author tangyi
 * @date 2022/5/1 3:16 下午
 */
@Slf4j
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

	private static final String PASSWORD = "password";

	private static final String CREDENTIAL = "credential";

	// all get access token uri
	public static final String ACCESS_TOKEN_URLS = EnvUtils.getValue("ACCESS_TOKEN_LOGIN_URLS", StringUtils.join(
			Stream.of(SecurityConstant.LOGIN_URL, SecurityConstant.REGISTER, SecurityConstant.MOBILE_LOGIN_URL)
					.toArray(), CommonConstant.COMMA));

	public static final String[] ACCESS_TOKEN_URLS_ARR = StringUtils.split(ACCESS_TOKEN_URLS, CommonConstant.COMMA);

	private final AuthenticationManager authenticationManager;

	private final UserTokenService userTokenService;

	private final SysProperties sysProperties;

	public TokenLoginFilter(AuthenticationManager authenticationManager, UserTokenService userTokenService,
			SysProperties sysProperties) {
		this.authenticationManager = authenticationManager;
		this.userTokenService = userTokenService;
		this.sysProperties = sysProperties;
		this.setPostOnly(false);
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse res)
			throws AuthenticationException {
		Map<String, String[]> map = request.getParameterMap();
		String username = map.get("username")[0];
		String credential = null;
		if (map.containsKey(CREDENTIAL)) {
			credential = map.get(CREDENTIAL)[0];
		}
		String password = null;
		if (map.containsKey(PASSWORD)) {
			password = map.get(PASSWORD)[0];
		}
		String decryptResult = decryptPassword(request, password, credential);
		return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, decryptResult, Lists.newArrayList()));
	}

	private String decryptPassword(HttpServletRequest request, String password, String credential) {
		String uri = request.getRequestURI();
		if (HttpMethod.POST.matches(request.getMethod()) && StrUtil.containsAnyIgnoreCase(uri, ACCESS_TOKEN_URLS_ARR)) {
			if (needDecrypt(request)) {
				if (StringUtils.isNotBlank(credential)) {
					password = doDecrypt(credential);
				} else if (StringUtils.isNotBlank(password)) {
					password = doDecrypt(password);
				}
			}
		}
		return password;
	}

	/**
	 * 授权类型为密码模式则解密
	 * @param request request
	 * @return boolean
	 */
	private boolean needDecrypt(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String grantType = request.getParameter(SecurityConstant.GRANT_TYPE);
		return CommonConstant.GRANT_TYPE_PASSWORD.equals(grantType) || StrUtil.containsAnyIgnoreCase(uri,
				SecurityConstant.REGISTER);
	}

	private String doDecrypt(String target) {
		try {
			target = target.replaceAll("%2B", "\\+");
			target = AesUtil.decryptAES(target, sysProperties.getKey()).trim();
		} catch (Exception e) {
			log.error("credential password fail, password: {}", target, e);
		}
		return target;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
		userTokenService.generateAndSaveToken(req, res, details);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
			AuthenticationException failed) throws IOException, ServletException {
		if (failed instanceof BadCredentialsException) {
			log.error("unsuccessful authentication: {}", failed.getMessage());
		} else {
			log.error("unsuccessful authentication", failed);
		}
		RUtil.out(res, R.error(failed.getMessage()));
	}
}
