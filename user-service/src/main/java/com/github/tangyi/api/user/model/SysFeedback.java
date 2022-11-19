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
 * 反馈信息 sys_feedback
 *
 * @author tangyi
 * @date 2022-08-16
 */
@Data
@Table(name = "sys_feedback")
@EqualsAndHashCode(callSuper = true)
public class SysFeedback extends BaseEntity<SysFeedback> {

	/**
	 * 提交人
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "submitter_id")
	private Long submitterId;

	/**
	 * 提交内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 反馈状态
	 */
	@Column(name = "status")
	private Integer status;

	public void setSubmitterId(Long submitterId) {
		this.submitterId = submitterId;
	}

	public Long getSubmitterId() {
		return submitterId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
