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
import com.github.tangyi.common.utils.SysUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 消息 sys_message
 */
@Data
@Table(name = "sys_message")
@EqualsAndHashCode(callSuper = true)
public class SysMessage extends BaseEntity<SysMessage> {

	/**
	 * 消息标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 消息内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 消息类型
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 接收人类型，0：全部用户，1：部分用户
	 */
	@Column(name = "receiver_type")
	private Integer receiverType;

	/**
	 * 发送人
	 */
	@Column(name = "sender")
	private String sender;

	/**
	 * 状态，0：草稿，1：已发布
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 接收人
	 */
	@Transient
	private List<Long> receivers;

	/**
	 * 接收部门 ID
	 */
	@Transient
	private String receiverDeptId;

	/**
	 * 是否已读
	 */
	@Transient
	private boolean hasRead;

	public static SysMessage of(String tenantCode, Integer status, Integer receiverType) {
		SysMessage message = new SysMessage();
		message.setTenantCode(tenantCode);
		message.setStatus(status);
		message.setReceiverType(receiverType);
		return message;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
