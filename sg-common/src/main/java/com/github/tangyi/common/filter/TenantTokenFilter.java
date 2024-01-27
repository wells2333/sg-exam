package com.github.tangyi.common.filter;

import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.utils.TenantHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantTokenFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String tenantCode = req.getHeader(CommonConstant.TENANT_CODE_HEADER);
		if (StringUtils.isBlank(tenantCode)) {
			tenantCode = req.getParameter(CommonConstant.TENANT_CODE);
		}

		try {
			if (StringUtils.isNotEmpty(tenantCode)) {
				TenantHolder.setTenantCode(tenantCode);
			}
			chain.doFilter(req, res);
		} finally {
			TenantHolder.clear();
		}
	}
}
