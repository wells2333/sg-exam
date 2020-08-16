package com.github.tangyi.auth.filter;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.AesUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解密过滤器
 * 对外密码字段的名称是credential，在这里解密，转换成password
 *
 * @author tangyi
 * @date 2019/3/18 11:30
 */
@Slf4j
@AllArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DecodePasswordFilter implements Filter {

	private static final String CREDENTIAL = "credential";

	private static final String PASSWORD = "password";

	private final SysProperties sysProperties;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper(request);
		// 请求的URI
		String uri = request.getRequestURI();
		// 获取token的请求
		if (HttpMethod.POST.matches(request.getMethod()) && StrUtil
				.containsAnyIgnoreCase(uri, SecurityConstant.OAUTH_TOKEN_URL, SecurityConstant.REGISTER,
						SecurityConstant.MOBILE_TOKEN_URL)) {
			String grantType = request.getParameter(SecurityConstant.GRANT_TYPE);
			// 授权类型为密码模式则解密
			if (CommonConstant.GRANT_TYPE_PASSWORD.equals(grantType) || StrUtil
					.containsAnyIgnoreCase(uri, SecurityConstant.REGISTER)) {
				String credential = request.getParameter(CREDENTIAL);
				String decrypt = "";
				if (StringUtils.isNotBlank(credential)) {
					try {
						// 开始解密
						decrypt = AesUtil.decryptAES(credential, sysProperties.getKey());
						decrypt = decrypt.trim();
						requestWrapper.addParameter(PASSWORD, decrypt);
						requestWrapper.addParameter(CREDENTIAL, decrypt);
					} catch (Exception e) {
						log.error("credential decrypt fail, credential:{}, decrypt:{}", credential, decrypt);
					}
				}
			}
		}
		filterChain.doFilter(requestWrapper, response);
	}
}
