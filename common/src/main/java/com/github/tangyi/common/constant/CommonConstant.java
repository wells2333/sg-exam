package com.github.tangyi.common.constant;

import com.github.tangyi.common.utils.EnvUtils;

/**
 * 公用常量
 *
 * @author tangyi
 * @date 2018-08-23 12:00
 */
public class CommonConstant {

	/**
	 * 正常
	 */
	public static final Integer STATUS_NORMAL = 0;

	/**
	 * 正常状态
	 */
	public static final Integer DEL_FLAG_NORMAL = 0;

	/**
	 * 默认密码
	 */
	public static final String DEFAULT_PASSWORD = EnvUtils.getValue("DEFAULT_PASSWORD", "123456");

	/**
	 * 保存code的前缀
	 */
	public static final String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY_";

	/**
	 * 密码类型
	 */
	public static final String GRANT_TYPE_PASSWORD = "password";

	public static final String TENANT_CODE_HEADER = "Tenant-Code";

	public static final String CACHE_EXPIRE = "CACHE_EXPIRE";

	public static final Long ROOT = -1L;

	public static final String BASE_PACKAGE = "com.github.tangyi";

	public static final String COMMA = ",";

	public static final String TENANT_CODE = "tenantCode";
}

