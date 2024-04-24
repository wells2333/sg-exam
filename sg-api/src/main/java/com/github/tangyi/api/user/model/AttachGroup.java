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

import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_attachment_group")
public class AttachGroup extends BaseEntity<AttachGroup> {

	@Column(name = "group_name")
	private String groupName;

	@Column(name = "group_code")
	private String groupCode;

	@Column(name = "storage_type")
	private Integer storageType;

	@Column(name = "url_expire")
	private Long urlExpire;

	@Column(name = "remark")
	private String remark;

	public static AttachGroup of(String groupCode, Integer storageType) {
		AttachGroup group = new AttachGroup();
		group.setGroupCode(groupCode);
		group.setStorageType(storageType);
		return group;
	}

	public static AttachGroup of(AttachTypeEnum typeEnum) {
		AttachGroup group = new AttachGroup();
		group.setGroupCode(typeEnum.getValue());
		group.setStorageType(typeEnum.getStorageType());
		return group;
	}
}
