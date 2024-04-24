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
import com.github.tangyi.api.user.model.Menu;
import com.github.tangyi.common.base.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class MenuDto extends TreeEntity<MenuDto> {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

	private String icon;

	private String name;

	private String path;

	private String redirect;

	private String component;

	private String permission;

	private String code;

	private Byte type;

	private String label;

	private String[] roles;

	private String remark;

	private Date createDate;

	private Integer isExt;

	private Integer keepalive;

	private Boolean hideMenu;

	private String currentActiveMenu;

	private MenuMetaDto meta;

	public MenuDto() {

	}

	public MenuDto(Menu menu) {
		try {
			BeanUtils.copyProperties(this, menu);
		} catch (Exception e) {
			log.error("copy properties failed", e);
		}
		this.meta = new MenuMetaDto();
		this.meta.setIcon(this.icon);
		this.meta.setTitle(this.name);
		this.meta.setOrderNo(this.sort);
		this.meta.setHideMenu(this.hideMenu);
		this.meta.setCurrentActiveMenu(this.currentActiveMenu);
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}
}
