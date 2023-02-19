package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
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
	 * 是否已读
	 */
	@Transient
	private boolean hasRead;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
