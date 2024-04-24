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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_role")
public class Role extends BaseEntity<Role> {

	@NotBlank(message = "角色名称不能为空")
	@Column(name = "role_name")
	private String roleName;

	@NotBlank(message = "角色标识不能为空")
	@Column(name = "role_code")
	private String roleCode;

	@Column(name = "role_desc")
	private String roleDesc;

	private Integer status;

	@Transient
	private String menuIds;

	/**
	 * 是否默认 0-否，1-是
	 */
	@Column(name = "is_default")
	private Integer isDefault;
}
