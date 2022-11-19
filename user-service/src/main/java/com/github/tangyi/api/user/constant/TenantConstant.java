package com.github.tangyi.api.user.constant;

import com.github.tangyi.common.utils.EnvUtils;

/**
 * @author tangyi
 * @date 2019/5/27 10:25
 */
public class TenantConstant {

	public static final String DEFAULT_TENANT_CODE = EnvUtils.getValue("DEFAULT_TENANT_CODE", "gitee");

    /**
     * 待审核
     */
    public static final Integer PENDING_AUDIT = 0;

    /**
     * 审核通过
     */
    public static final Integer APPROVAL = 1;

    /**
     * 审核不通过
     */
    public static final Integer AUDIT_FAIL = 2;

	public static final Integer NOT_INIT = 0;

	public static final Integer INIT_ING = 1;

	public static final Integer INIT_SUCCESS = 2;

	public static final Integer INIT_FAILED = 3;

}
