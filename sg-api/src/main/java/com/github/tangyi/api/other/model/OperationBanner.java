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

package com.github.tangyi.api.other.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 首页运营位 exam_operation_banner
 */
@Data
@Table(name = "operation_banner")
@EqualsAndHashCode(callSuper = true)
public class OperationBanner extends BaseEntity<OperationBanner> {

	/**
	 * 运营位名称
	 */
	@Column(name = "operation_name")
	private String operationName;

	/**
	 * 运营位类型
	 */
	@Column(name = "operation_type")
	private Integer operationType;

	/**
	 * 图片 ID
	 */
	@Column(name = "image_id")
	private String imageId;

	/**
	 * 图片 URL
	 */
	@Column(name = "image_url")
	private String imageUrl;

	/**
	 * 跳转的 URL
	 */
	@Column(name = "redirect_url")
	private String redirectUrl;

	/**
	 * 备注
	 */
	@Column(name = "operation_desc")
	private String operationDesc;

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public String getOperationDesc() {
		return operationDesc;
	}

	public void setOperationDesc(String operationDesc) {
		this.operationDesc = operationDesc;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
