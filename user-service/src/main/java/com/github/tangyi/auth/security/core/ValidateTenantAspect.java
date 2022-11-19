package com.github.tangyi.auth.security.core;

import com.github.tangyi.api.user.model.Tenant;
import com.github.tangyi.common.exceptions.TenantNotFoundException;
import com.github.tangyi.user.service.TenantService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 *
 * 校验租户
 *
 * @author tangyi
 * @date 2019-11-12 20:14
 */
@AllArgsConstructor
@Aspect
@Component
public class ValidateTenantAspect {

	private final TenantService tenantService;

	@Before("execution(* com.github.tangyi.auth.security.core.user.CustomUserDetailsServiceImpl.*AndTenantCode(..)) && args(tenantCode,..)")
	public void validateTenantCode(String tenantCode) throws TenantNotFoundException {
		if (StringUtils.isBlank(tenantCode)) {
			throw new TenantNotFoundException("获取租户信息失败");
		}
		// 获取租户信息
		Tenant tenant = tenantService.getByTenantCode(tenantCode);
		if (tenant == null) {
			throw new TenantNotFoundException(String.format("租户 %s 不存在", tenantCode));
		}
	}
}
