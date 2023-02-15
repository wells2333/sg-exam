package com.github.tangyi.common.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

public class SgPreCondition {

	public static void checkIdentifierAndTenantCode(String identifier, String tenantCode) {
		Preconditions.checkState(StringUtils.isNotEmpty(identifier), "identifier must not be null");
		checkTenantCode(tenantCode);
	}

	public static void checkUserIdAndTenantCode(Long userId, String tenantCode) {
		Preconditions.checkState(userId != null, "userId must not be null");
		checkTenantCode(tenantCode);
	}

	public static void checkTenantCode(String tenantCode) {
		Preconditions.checkState(StringUtils.isNotEmpty(tenantCode), "tenantCode must not be null");
	}
}
