package com.github.tangyi.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型
 *
 * @author tangyi
 * @date 2019/07/02 09:45
 */
@Getter
@AllArgsConstructor
public enum LoginType {

    /**
     * 账号密码登录
     */
    PWD("PWD", "账号密码登录"),

    /**
     * 验证码登录
     */
    SMS("SMS", "验证码登录"),

    /**
     * QQ登录
     */
    QQ("QQ", "QQ登录"),

    /**
     * 微信登录
     */
    WECHAT("WX", "微信登录");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;
}
