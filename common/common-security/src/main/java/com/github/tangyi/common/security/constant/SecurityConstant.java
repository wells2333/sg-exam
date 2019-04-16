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
     * 管理员角色
     */
    public static final String ROLE_ADMIN = "role_admin";

    /**
     * 老师角色
     */
    public static final String ROLE_TEACHER = "role_teacher";

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

    /**
     * 正常状态
     */
    public static final String NORMAL = "0";

    /**
     * 异常状态
     */
    public static final String ABNORMAL = "1";
}
