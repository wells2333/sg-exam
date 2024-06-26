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


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.common.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Slf4j
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "用户返回结果")
public class UserDto implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ
	 */
	@Schema(description = "授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ", example = "1")
	private Integer identityType;

	/**
	 * 唯一标识，如用户名、手机号
	 */
	@Schema(description = "账号唯一标识，如用户名、手机号", example = "git")
	private String identifier;

	/**
	 * 密码凭证，跟授权类型有关，如密码、第三方系统的 token 等
	 */
	@Schema(description = "密码，需要使用 AES 加密", example = "lBTqrKS0kZixOFXeZ0HRng==")
	private String credential;

	/**
	 * 角色
	 */
	@Schema(description = "角色", hidden = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private List<Long> role = new ArrayList<>();

	/**
	 * 部门 ID
	 */
	@Schema(description = "部门 ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long deptId;

	/**
	 * 部门名称
	 */
	@Schema(description = "部门名称", hidden = true)
	private String deptName;

	/**
	 * 旧密码
	 */
	@Schema(description = "旧密码", hidden = true)
	private String oldPassword;

	/**
	 * 新密码
	 */
	@Schema(description = "新密码", hidden = true)
	private String newPassword;

	/**
	 * 姓名
	 */
	@Schema(description = "姓名", example = "git")
	private String name;

	/**
	 * 电话
	 */
	@Schema(description = "电话", example = "15521089184")
	@Pattern(regexp = "^\\d{11}$", message = "请输入 11 位手机号")
	private String phone;

	/**
	 * 头像 id
	 */
	@Schema(description = "头像 id", hidden = true)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long avatarId;

	/**
	 * 头像 URL
	 */
	@Schema(description = "头像 URL", hidden = true)
	private String avatarUrl;

	/**
	 * 邮箱
	 */
	@Schema(description = "邮箱", example = "1633736729@qq.com")
	@Email(message = "邮箱格式不正确")
	private String email;

	/**
	 * 性别
	 */
	@Schema(description = "性别，0：男，1：女", example = "0")
	private Integer gender;

	private String genderName;

	/**
	 * 出生日期
	 */
	@Schema(description = "出生日期")
	private Date born;

	/**
	 * 描述
	 */
	@Schema(description = "描述", example = "git")
	private String userDesc;

	/**
	 * 状态
	 */
	@Schema(description = "状态，0：启用，1：禁用", example = "0")
	private Integer status;

	/**
	 * 角色列表
	 */
	@Schema(description = "角色列表", hidden = true)
	private String roleNames;

	protected Date createTime;

	protected Date updateTime;

	/**
	 * 租户标识
	 */
	@Schema(description = "租户标识", example = "gitee")
	private String tenantCode;

	/**
	 * 引导注册人
	 */
	@Schema(description = "引导注册人")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentUid;

	/**
	 * 乡/镇
	 */
	@Schema(description = "乡/镇")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long streetId;

	/**
	 * 县
	 */
	@Schema(description = "县")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long countyId;

	/**
	 * 城市
	 */
	@Schema(description = "城市")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long cityId;

	/**
	 * 省份
	 */
	@Schema(description = "省份")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long provinceId;

	/**
	 * 最近登录时间
	 */
	@Schema(description = "最近登录时间", hidden = true)
	private Date loginTime;

	/**
	 * 用户归档时间
	 */
	@Schema(description = "用户归档时间", hidden = true)
	private Date lockTime;

	/**
	 * 微信
	 */
	@Schema(description = "微信")
	private String wechat;

	/**
	 * 家庭角色
	 */
	@Schema(description = "家庭角色，0：爸爸，1：妈妈，2：爷爷，3：奶奶，5：外公，6：外婆", example = "0")
	private Integer familyRole;

	/**
	 * 个性签名
	 */
	@Schema(description = "个性签名")
	private String signature;

	/**
	 * 短信验证码
	 */
	private String code;

	public UserDto() {

	}

	public UserDto(User user) {
		if (user != null) {
			try {
				BeanUtils.copyProperties(user, this);
			} catch (Exception e) {
				log.error("init userDto failed", e);
			}
			this.genderName = GenderEnum.matchByValue(user.getGender()).getName();
		}
	}
}
