package com.github.tangyi.common.constant;

/**
 * @author tangyi
 * @date 2018-08-25 14:08
 */
public class SecurityConstant {

    /**
     * 租户管理员角色
     */
    public static final String ROLE_TENANT_ADMIN = "role_tenant_admin";

    /**
     * 默认生成图形验证码过期时间
     */
    public static final int DEFAULT_IMAGE_EXPIRE = 60;

    /**
     * 默认短信验证码过期时间
     */
    public static final int DEFAULT_SMS_EXPIRE = 15 * 60;

    /**
     * 正常状态
     */
    public static final String NORMAL = "0";

    /**
     * 手机登录URL
     */
    public static final String MOBILE_TOKEN_URL = "/mobile/token";

    /**
     * 微信登录URL
     */
    public static final String WX_TOKEN_URL = "/wx/token";

    /**
     * JSON 资源
     */
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";

	/**
	 * 登录类型
	 */
	public static final String LOGIN_TYPE = "loginType";
}
