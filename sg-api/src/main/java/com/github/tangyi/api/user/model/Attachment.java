package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 附件信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_attachment")
public class Attachment extends BaseEntity<Attachment> {

	/**
	 * 附件名称
	 */
	@Column(name = "attach_name")
	private String attachName;

	/**
	 * 附件类型
	 */
	@Column(name = "attach_type")
	private String attachType;

	/**
	 * 附件大小
	 */
	@Column(name = "attach_size")
	private String attachSize;

	/**
	 * 分组
	 */
	@Column(name = "group_code")
	private String groupCode;

	/**
	 * 预览地址
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 上传结果
	 */
	@Transient
	private String uploadResult;
}
