package com.github.tangyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    /**
     * 账号密码登录
     */
    PWD("PWD", "账号密码登录", "/oauth/token"),

    /**
     * 验证码登录
     */
    SMS("SMS", "验证码登录", "/mobile/token"),

    /**
     * QQ 登录
     */
    QQ("QQ", "QQ 登录", "/mobile/token"),

    /**
     * 微信登录
     */
    WECHAT("WX", "微信登录", "/wx/token");

    /**
     * 类型
     */
    private final String type;

    /**
     * 描述
     */
    private final String description;

    /**
     * 接口 uri
     */
    private final String uri;
}
