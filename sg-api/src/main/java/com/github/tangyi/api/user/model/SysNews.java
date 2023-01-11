package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "sys_news")
@EqualsAndHashCode(callSuper = true)
public class SysNews extends BaseEntity<SysNews> {

	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 跳转页面
	 */
	@Column(name = "location")
	private String location;

	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
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
