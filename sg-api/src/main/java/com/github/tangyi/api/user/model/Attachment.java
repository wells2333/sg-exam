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
	 * 哈希值
	 */
	@Column(name = "hash")
	private String hash;

	/**
	 * 多分片上传的 uploadId
	 */
	@Column(name = "upload_id")
	private String uploadId;

	/**
	 * 上传结果
	 */
	@Transient
	private String uploadResult;
}
