package com.github.tangyi.common.filter;

import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.utils.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 获取请求里的租户code
 *
 * @author tangyi
 * @date 2019/5/28 22:53
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantTokenFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		// 获取请求头里的TENANT_CODE
		String tenantCode = request.getHeader(CommonConstant.TENANT_CODE_HEADER);
		if (StringUtils.isBlank(tenantCode)) {
			tenantCode = request.getParameter(CommonConstant.TENANT_CODE);
		}
		if (StringUtils.isNotEmpty(tenantCode)) {
			TenantContextHolder.setTenantCode(tenantCode);
		}
		filterChain.doFilter(request, response);
		TenantContextHolder.clear();
	}
}
