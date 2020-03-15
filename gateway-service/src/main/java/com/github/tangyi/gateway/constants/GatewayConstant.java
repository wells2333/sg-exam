package com.github.tangyi.gateway.constants;

/**
 * @author tangyi
 * @date 2019/3/18 16:55
 */
public class GatewayConstant {

    /**
     * oauth token
     */
    public static final String OAUTH_TOKEN_URL = "/oauth/token";

    /**
     * 手机登录URL
     */
    public static final String MOBILE_TOKEN_URL = "/mobile/token";

    /**
     * 微信登录URL
     */
    public static final String WX_TOKEN_URL = "/wx/token";
    
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

    /**
     * 默认系统编号
     */
    public static final String SYS_CODE = "EXAM";

    /**
     * 默认租户编号
     */
    public static final String DEFAULT_TENANT_CODE = "gitee";

    public static final String TENANT_CODE_HEADER = "Tenant-Code";

}
