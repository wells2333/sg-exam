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
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 系统配置 sys_config
 */
@Data
@Table(name = "sys_config")
@EqualsAndHashCode(callSuper = true)
public class SysConfig extends BaseEntity<SysConfig> {

	/**
	 * 配置 key
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "config_key")
	private String configKey;

	/**
	 * 配置的值
	 */
	@Column(name = "config_value")
	private String configValue;

	/**
	 * 配置描述
	 */
	@Column(name = "config_desc")
	private String configDesc;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
