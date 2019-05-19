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
     * 注册
     */
    public static final String REGISTER = "/user/register";

    /**
     * 密码类型
     */
    public static final String GRANT_TYPE_PASSWORD = "password";

    /**
     * 刷新token类型
     */
    public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    /**
     * 授权类型
     */
    public static final String GRANT_TYPE = "grant_type";

    /**
     * 验证码已过期，请重新获取
     */
    public static final String EXPIRED_ERROR = "验证码已过期，请重新获取";

    /**
     * 网关缓存的access_token的名称
     */
    public static final String GATEWAY_ACCESS_TOKENS = "gateway_access:";

    /**
     * 网关缓存的refresh_token的名称
     */
    public static final String GATEWAY_REFRESH_TOKENS = "gateway_refresh:";

}
