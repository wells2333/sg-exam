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

package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_user")
public class User extends BaseEntity<User> {

	/**
	 * 姓名
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 电话
	 */
	@Pattern(regexp = "^\\d{11}$", message = "请输入 11 位手机号")
	@Column(name = "phone")
	private String phone;

	/**
	 * 头像 ID
	 */
	@Column(name = "avatar_id")
	private Long avatarId;

	/**
	 * 邮箱
	 */
	@Email(message = "邮箱格式不正确")
	@Column(name = "email")
	private String email;

	/**
	 * 性别
	 */
	@Column(name = "gender")
	private Integer gender;

	/**
	 * 出生日期
	 */
	@Column(name = "born")
	private Date born;

	/**
	 * 描述
	 */
	@Column(name = "user_desc")
	private String userDesc;

	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 部门 ID
	 */
	@Column(name = "dept_id")
	private Long deptId;

	/**
	 * 角色列表
	 */
	private List<Role> roleList;

	/**
	 * 角色
	 */
	private List<Long> role;

	/**
	 * 引导注册人
	 */
	@Column(name = "parent_uid")
	private Long parentUid;

	/**
	 * 乡/镇
	 */
	@Column(name = "street_id")
	private Long streetId;

	/**
	 * 县
	 */
	@Column(name = "county_id")
	private Long countyId;

	/**
	 * 城市
	 */
	@Column(name = "city_id")
	private Long cityId;

	/**
	 * 省份
	 */
	@Column(name = "province_id")
	private Long provinceId;

	/**
	 * 最近登录时间
	 */
	@Column(name = "login_time")
	private Date loginTime;

	/**
	 * 用户归档时间
	 */
	@Column(name = "lock_time")
	private Date lockTime;

	/**
	 * 微信
	 */
	@Column(name = "wechat")
	private String wechat;

	/**
	 * 家庭角色，参考 UserStudentConstant
	 */
	@Column(name = "family_role")
	private Integer familyRole;

	/**
	 * 个性签名
	 */
	@Column(name = "signature")
	private String signature;
}
