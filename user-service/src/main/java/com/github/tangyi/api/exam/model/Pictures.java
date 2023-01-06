package com.github.tangyi.api.exam.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 图片表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_pictures")
public class Pictures extends BaseEntity<Pictures> {

	/**
	 * 图片地址
	 */
	@Column(name = "picture_address")
	private String pictureAddress;

	/**
	 * 附件ID
	 */
	@Column(name = "attachment_id")
	private String attachmentId;
}
