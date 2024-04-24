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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * 部门
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_dept")
public class Dept extends BaseEntity<Dept> {

	/**
	 * 部门名称
	 */
	@NotBlank(message = "部门名称不能为空")
	@Column(name = "dept_name")
	private String deptName;

	/**
	 * 部门描述
	 */
	@Column(name = "dept_desc")
	private String deptDesc;

	/**
	 * 部门负责人
	 */
	@Column(name = "dept_leader")
	private String deptLeader;

	/**
	 * 父部门 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "parent_id")
	private Long parentId;

	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;
}
