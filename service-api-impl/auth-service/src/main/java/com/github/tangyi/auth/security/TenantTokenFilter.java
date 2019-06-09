package com.github.tangyi.auth.security;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * 获取请求里的租户code
 *
 * @author tangyi
 * @date 2019/5/28 22:53
 */
@Slf4j
public class TenantTokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String tenantCode = servletRequest.getParameter(CommonConstant.TENANT_CODE);
        if (tenantCode != null)
            TenantContextHolder.setTenantCode(tenantCode);
        log.info("租户code：{}", tenantCode);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
