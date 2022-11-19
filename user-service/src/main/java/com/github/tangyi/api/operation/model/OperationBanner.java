package com.github.tangyi.api.operation.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 首页运营位 exam_operation_banner
 *
 * @author tangyi
 * @date 2022-11-12
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
	 * 图片URL
	 */
	@Column(name = "image_url")
	private String imageUrl;

	/**
	 * 跳转的URL
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
