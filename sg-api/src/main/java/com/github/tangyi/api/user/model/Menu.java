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
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_menu")
public class Menu extends BaseEntity<Menu> {

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String name;

	/**
	 * 类型（1 菜单，2 权限)
	 */
	private Byte type;

	/**
	 * 组件路径
	 */
	private String component;

    /**
     * 路由地址
     */
    private String path;

	/**
	 * 重定向地址
	 */
	private String redirect;

	/**
	 * 权限标识
	 */
	private String permission;

    /**
     * 父菜单 ID
     */
	@Column(name = "parent_id")
    private Long parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

	/**
	 * 是否外链
	 */
	@Column(name = "is_ext")
	private Integer isExt;

	/**
	 * 是否缓存
	 */
	private Integer keepalive;

	/**
	 * 是否显示，1：显示，0：不显示
	 */
	@Column(name = "hide_menu")
	private Boolean hideMenu;

	@Column(name = "current_active_menu")
	private String currentActiveMenu;
}
