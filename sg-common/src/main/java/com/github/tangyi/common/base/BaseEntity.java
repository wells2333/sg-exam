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

package com.github.tangyi.common.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.utils.SysUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BaseEntity<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	@JsonSerialize(using = ToStringSerializer.class)
	protected Long id;

	protected String creator;

	@Column(name = "create_time")
	protected Date createTime;

	protected String operator;

	@Column(name = "update_time")
	protected Date updateTime;

	/**
	 * 逻辑删除标记：0 正常，1 删除
	 */
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "tenant_code")
	protected String tenantCode;

	protected boolean isNewRecord;

	public boolean isNewRecord() {
		return this.isNewRecord || this.getId() == null;
	}

	public void setCommonValue() {
		setCommonValue(SysUtil.getUser(), SysUtil.getTenantCode());
	}

	public void setCommonValue(String userCode, String tenantCode) {
		Date now = new Date();
		if (this.isNewRecord()) {
			this.createTime = now;
			this.creator = userCode;
		}
		if (this.updateTime == null) {
			this.updateTime = now;
		}
		this.operator = userCode;
		if (this.isDeleted == null) {
			this.isDeleted = Boolean.FALSE;
		}
		this.tenantCode = tenantCode;
	}

	public void clearCommonValue() {
		this.creator = null;
		this.createTime = null;
		this.operator = null;
		this.updateTime = null;
		this.isDeleted = null;
		this.tenantCode = null;
	}

	@SuppressWarnings({"rawtypes"})
	public static <E extends BaseEntity> Long[] ids(List<E> entities) {
		return entities.stream().map(BaseEntity::getId).toArray(Long[]::new);
	}
}

