package com.github.tangyi.common.constant;

import com.github.tangyi.common.utils.EnvUtils;

public interface CommonConstant {

	String DEFAULT_PASSWORD = EnvUtils.getValue("DEFAULT_PASSWORD", "123456");

	Integer STATUS_NORMAL = 0;

	Integer DEL_FLAG_NORMAL = 0;

	String VERIFICATION_CODE_KEY = "VERIFICATION_CODE_KEY_";

	String GRANT_TYPE_PASSWORD = "password";

	String TENANT_CODE_HEADER = "Tenant-Code";

	Long ROOT = -1L;

	String COMMA = ",";

	String TENANT_CODE = "tenantCode";
}

