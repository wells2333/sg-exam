package com.github.tangyi.common.security.constant;

/**
 * @author tangyi
 * @date 2018-08-25 14:08
 */
public class SecurityConstant {

    /**
     * 基础角色
     */
    public static final String BASE_ROLE = "ROLE_USER";

    /**
     * 管理员角色
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * 老师角色
     */
    public static final String ROLE_TEACHER = "ROLE_TEACHER";

    /**
     * token
     */
    public static final String TOKEN_USER_DETAIL = "token-user-detail";

    /**
     * 默认生成图形验证码过期时间
     */
    public static final int DEFAULT_IMAGE_EXPIRE = 60;

    /**
     * oauth 客户端信息
     */
    public static final String CLIENT_DETAILS_KEY = "exam_oauth:client:details";
}
