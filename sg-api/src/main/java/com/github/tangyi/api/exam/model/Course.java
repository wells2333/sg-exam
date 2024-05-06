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

package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Table(name = "exam_course")
@EqualsAndHashCode(callSuper = true)
public class Course extends BaseEntity<Course> {

	/**
	 * 课程名称
	 */
	@NotBlank(message = "课程名称不能为空")
	@Column(name = "course_name")
	private String courseName;

	/**
	 * 学院
	 */
	private String college;

	/**
	 * 专业
	 */
	private String major;

	/**
	 * 老师
	 */
	private String teacher;

	/**
	 * 课程描述
	 */
	@Column(name = "course_description")
	private String courseDescription;

	/**
	 * logoId
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "image_id")
	private Long imageId;

	/**
	 * image URL
	 */
	private String imageUrl;

	/**
	 * 收费类型：0：免费，1：收费，默认免费
	 */
	@Column(name = "charge_type")
	private Integer chargeType;

	/**
	 * 收费价格
	 */
	@Column(name = "charge_price")
	private Double chargePrice;

	/**
	 * 难度等级，1~5，默认 3
	 */
	@Column(name = "level")
	private Integer level;

	/**
	 * 简短版描述
	 */
	@Column(name = "simple_desc")
	private String simpleDesc;

	/**
	 * 排序号
	 */
	@Column(name = "sort")
	private Integer sort;

	/**
	 * 课程状态，0：上架，1：下架
	 */
	@Column(name = "course_status")
	private Integer courseStatus;

	/**
	 * 课件
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "attach_id")
	private Long attachId;

	/**
	 * 权限控制，0：全部用户，1：指定用户，2：指定部门
	 */
	@Column(name = "access_type")
	private Integer accessType;

	/**
	 * 报名人数
	 */
	@Transient
	private Integer memberCount;

	/**
	 * 是否收藏
	 */
	@Transient
	private Boolean favorite;

	/**
	 * 收藏人数
	 */
	@Transient
	private Long favCount;

	/**
	 * 课件 URL
	 */
	@Transient
	private String attachUrl;

	/**
	 * 成员 ID
	 */
	@Transient
	private List<Long> members;

	/**
	 * 部门 ID
	 */
	@Transient
	private String deptMember;
}
