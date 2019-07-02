package com.github.tangyi.common.security.constant;

/**
 * @author tangyi
 * @date 2018-08-25 14:08
 */
public class SecurityConstant {

    /**
     * 基础角色
     */
    public static final String BASE_ROLE = "role_user";

    /**
     * 超级管理员角色
     */
    public static final String ROLE_ADMIN = "role_admin";

    /**
     * 默认生成图形验证码过期时间
     */
    public static final int DEFAULT_IMAGE_EXPIRE = 60;

    /**
     * 默认短信验证码过期时间
     */
    public static final int DEFAULT_SMS_EXPIRE = 5 * 60;

    /**
     * 正常状态
     */
    public static final String NORMAL = "0";

    /**
     * 异常状态
     */
    public static final String ABNORMAL = "1";

    /**
     * 手机登录URL
     */
    public static final String MOBILE_TOKEN_URL = "/mobile/token";

    /**
     * 租户编号请求头
     */
    public static final String TENANT_CODE_HEADER = "Tenant-Code";

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
}
