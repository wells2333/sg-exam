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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

/**
 * 消息接收 sys_message_receiver
 */
@Data
@Table(name = "sys_message_receiver")
@EqualsAndHashCode(callSuper = true)
public class SysMessageReceiver extends BaseEntity<SysMessageReceiver> {

	/**
	 * 消息 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "message_id")
	private Long messageId;

	/**
	 * 接收人 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "receiver_id")
	private Long receiverId;

	@Column(name = "type")
	private Integer type;

	public static SysMessageReceiver of(String user, String tenantCode, Long messageId, Long receiverId, Integer type) {
		SysMessageReceiver receiver = new SysMessageReceiver();
		receiver.setCommonValue(user, tenantCode);
		receiver.setReceiverId(receiverId);
		receiver.setMessageId(messageId);
		receiver.setType(type);
		return receiver;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
