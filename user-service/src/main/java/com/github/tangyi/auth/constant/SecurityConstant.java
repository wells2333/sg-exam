package com.github.tangyi.auth.constant;

public class SecurityConstant {

    /**
     * 手机登录URL
     */
    public static final String MOBILE_LOGIN_URL = "/mobile/login";

    /**
     * 微信登录URL
     */
    public static final String WX_LOGIN_URL = "/wx/login";

    /**
     * 默认系统编号
     */
    public static final String SYS_CODE = "EXAM";

    /**
     * 默认租户编号
     */
    public static final String DEFAULT_TENANT_CODE = "gitee";

    /**
     * 租户编号
     */
    public static final String TENANT_CODE = "tenantCode";

    /**
     * JSON 资源
     */
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";

	/**
	 * 登录URL
	 */
	public static final String LOGIN_URL = "/login";

	/**
	 * 注册
	 */
	public static final String REGISTER = "/user/anonymousUser/register";

	/**
	 * 授权类型
	 */
	public static final String GRANT_TYPE = "grant_type";

	/**
	 * 验证码已过期，请重新获取
	 */
	public static final String EXPIRED_ERROR = "验证码已过期，请重新获取";
}
