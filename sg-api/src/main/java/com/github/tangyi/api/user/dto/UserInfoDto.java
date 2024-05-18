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

package com.github.tangyi.api.user.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class UserInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ
	 */
	private Integer identityType;

	/**
	 * 唯一标识，如用户名、手机号
	 */
	private String identifier;

	/**
	 * 密码
	 */
	@JsonIgnore
	private String credential;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 电话号码
	 */
	private String phone;

	/**
	 * 头像对应的附件 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long avatarId;

	/**
	 * 头像地址
	 */
	private String avatar;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 性别
	 */
	private Integer gender;

	/**
	 * 生日
	 */
	private Date born;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 部门 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long deptId;

	/**
	 * 备注
	 */
	private String userDesc;

	/**
	 * 状态，0-启用，1-禁用
	 */
	private Integer status;

	/**
	 * 权限信息
	 */
	private String[] permissions;

	/**
	 * 角色信息
	 */
	private String[] roles;

	/**
	 * 租户标识
	 */
	private String tenantCode;

	/**
	 * 引导注册人
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentUid;

	/**
	 * 乡/镇
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long streetId;

	/**
	 * 县
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long countyId;

	/**
	 * 城市
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long cityId;

	/**
	 * 省份
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long provinceId;

	/**
	 * 最近登录时间
	 */
	private Date loginTime;

	/**
	 * 用户归档时间
	 */
	private Date lockTime;

	/**
	 * 微信
	 */
	private String wechat;

	/**
	 * 个性签名
	 */
	private String signature;

	/**
	 * 用户菜单
	 */
	private List<MenuDto> menus;
}
