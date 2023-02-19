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
 * 已读消息 sys_message_read
 */
@Data
@Table(name = "sys_message_read")
@EqualsAndHashCode(callSuper = true)
public class SysMessageRead extends BaseEntity<SysMessageRead> {

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
