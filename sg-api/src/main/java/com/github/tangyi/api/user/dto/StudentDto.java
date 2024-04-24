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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StudentDto implements Serializable {

	private static final long serialVersionUID = 2602249526687821147L;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 用户 ID
	 */
	@Schema(description = "用户 ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;

	/**
	 * 学生 id
	 */
	@Schema(description = "学生 ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long studentId;

	/**
	 * 关系类型
	 */
	@Schema(description = "关系类型")
	private Integer relationshipType;

	/**
	 * 学生姓名
	 */
	@Schema(description = "学生姓名")
	private String studentName;

	/**
	 * 出生日期
	 */
	@Schema(description = "出生日期")
	private Date born;

	/**
	 * 电话号码
	 */
	@Schema(description = "电话", example = "15521089184")
	private String phone;

	/**
	 * 微信
	 */
	@Schema(description = "微信")
	private String wechat;

	/**
	 * 性别
	 */
	@Schema(description = "性别，0：男，1：女", example = "0")
	private Integer gender;

	/**
	 * 家庭住址
	 */
	@Schema(description = "家庭住址")
	private String address;

	/**
	 * 就读年级
	 */
	@Schema(description = "就读年级 ID")
	private String grade;


	/**
	 * 就读年级
	 */
	@Schema(description = "就读年级名称")
	private String gradeName;

	/**
	 * 就读学校
	 */
	@Schema(description = "就读学校")
	private String school;

	/**
	 * 就读城市
	 */
	@Schema(description = "就读城市 ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long cityId;

	/**
	 * 县 id
	 */
	@Schema(description = "就读区县 ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long countyId;
}
