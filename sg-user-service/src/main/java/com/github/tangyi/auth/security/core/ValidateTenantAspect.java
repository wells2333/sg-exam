/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.auth.security.core;

import com.github.tangyi.api.user.model.Tenant;
import com.github.tangyi.common.exceptions.TenantNotFoundException;
import com.github.tangyi.user.service.sys.TenantService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Aspect
@Component
public class ValidateTenantAspect {

	private final TenantService tenantService;

	@Before("execution(* com.github.tangyi.auth.security.core.user.CustomUserDetailsServiceImpl.*AndTenantCode(..)) && args(tenantCode,..)")
	public void validateTenantCode(String tenantCode) throws TenantNotFoundException {
		if (StringUtils.isBlank(tenantCode)) {
			throw new TenantNotFoundException("Failed to get tenant info.");
		}

		// 获取租户信息
		Tenant tenant = tenantService.getByTenantCode(tenantCode);
		if (tenant == null) {
			throw new TenantNotFoundException("The tenant " + tenantCode + " not found.");
		}
	}
}
