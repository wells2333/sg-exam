package com.github.tangyi.common.core.utils;

import com.github.tangyi.common.core.constant.CommonConstant;

/**
 * 系统工具类
 *
 * @author tangyi
 * @date 2018-09-13 20:50
 */
public class SysUtil {

    private static final String KEY_USER = "user";

    private static final String BASIC_ = "Basic ";

    /**
     * 从thread local 获取用户名
     *
     * @return 用户名
     */

    public static String getUser() {
        return null;
    }

    /**
     * 获取系统编号
     *
     * @return String
     */
    public static String getSysCode() {
        return CommonConstant.SYS_CODE;
    }

    /**
     * 获取租户编号
     *
     * @return String
     */
    public static String getTenantCode() {
        return CommonConstant.TENANT_CODE;
    }
}
