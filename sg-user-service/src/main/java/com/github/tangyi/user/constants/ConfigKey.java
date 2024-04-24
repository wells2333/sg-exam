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

package com.github.tangyi.user.constants;

public interface ConfigKey {

	/**
	 * web 首页网站名称
	 */
	String SYS_WEB_NAME = "sys_web_name";

	/**
	 * web 网站首页主标题
	 */
	String SYS_WEB_MAIN_TITLE = "sys_web_main_title";

	/**
	 * web 网站首页副标题
	 */
	String SYS_WEB_SUB_TITLE_ONE = "sys_web_sub_title_one";

	/**
	 * web 网站首页副标题
	 */
	String SYS_WEB_SUB_TITLE_TWO = "sys_web_sub_title_two";

	/**
	 * 网站 Copyright
	 */
	String SYS_WEB_COPYRIGHT = "sys_web_copyright";

	/**
	 * 是否显示网站广告位
	 */
	String SYS_WEB_SHOW_BANNER = "sys_web_show_banner";

	/**
	 * 系统 LOGO
	 */
	String SYS_AVATAR = "sys_avatar";

	/**
	 * 后台首页主标题
	 */
	String SYS_ADMIN_MAIN_TITLE = "sys_admin_main_title";

	/**
	 * 后台首页副标题
	 */
	String SYS_ADMIN_SUB_TITLE = "sys_admin_sub_title";

	/**
	 * 小程序首页封面图片
	 */
	String SYS_WXAPP_AVATAR = "sys_wxapp_avatar";

	/**
	 * 小程序首页主标题
	 */
	String SYS_WXAPP_MAIN_TITLE = "sys_wxapp_main_title";

	/**
	 * 小程序首页副标题
	 */
	String SYS_WXAPP_SUB_TITLE = "sys_wxapp_sub_title";

	/**
	 * 登录页面是否展示单位标识输入框
	 */
	String SYS_LOGIN_SHOW_TENANT_CODE = "sys_login_show_tenant_code";

	/**
	 * 附件预览地址
	 */
	String SYS_FILE_PREVIEW_URL = "sys_file_preview_url";
}
