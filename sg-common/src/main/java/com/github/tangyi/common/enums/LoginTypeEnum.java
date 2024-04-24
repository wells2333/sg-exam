/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
