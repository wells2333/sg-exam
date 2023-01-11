package com.github.tangyi.api.user.constant;

import com.github.tangyi.common.utils.EnvUtils;

public class TenantConstant {

	public static final String DEFAULT_TENANT_CODE = EnvUtils.getValue("DEFAULT_TENANT_CODE", "gitee");

    /**
     * 待审核
     */
    public static final Integer PENDING_AUDIT = 0;

	public static final Integer NOT_INIT = 0;

	public static final Integer INIT_ING = 1;

	public static final Integer INIT_SUCCESS = 2;

	public static final Integer INIT_FAILED = 3;

}
