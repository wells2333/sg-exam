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
@Table(name = "sys_tenant")
public class Tenant extends BaseEntity<Tenant> {

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
	@Column(name = "tenant_name")
    private String tenantName;

    /**
     * 租户描述信息
     */
	@Column(name = "tenant_desc")
    private String tenantDesc;

    /**
     * 状态，0-待审核，1-正常，2-审核不通过
     */
    private Integer status;

	/**
	 * 初始化状态，0-未初始化，1：初始化中，2：初始化完成，3：初始化失败
	 */
	@Column(name = "init_status")
	private Integer initStatus;

	/**
	 * 默认角色 ID，用于菜单分配
	 */
	@Column(name = "role_id")
	private Long roleId;

	/**
	 * 图片 ID
	 */
	@Column(name = "image_id")
	private Long imageId;

	/**
	 * 菜单 ID
	 */
	@Transient
	private String menuIds;

	@Transient
	private String imageUrl;
}
