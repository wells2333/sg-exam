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
	 * 消息ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "message_id")
	private Long messageId;

	/**
	 * 接收人ID
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
