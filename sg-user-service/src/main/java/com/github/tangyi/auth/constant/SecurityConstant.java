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

package com.github.tangyi.auth.constant;

public class SecurityConstant {

    /**
     * 手机登录 URL
     */
    public static final String MOBILE_LOGIN_URL = "/mobile/login";

    /**
     * 微信登录 URL
     */
    public static final String WX_LOGIN_URL = "/wx/login";

    /**
     * 默认租户编号
     */
    public static final String DEFAULT_TENANT_CODE = "gitee";

	/**
	 * 登录 URL
	 */
	public static final String LOGIN_URL = "/login";

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
}
