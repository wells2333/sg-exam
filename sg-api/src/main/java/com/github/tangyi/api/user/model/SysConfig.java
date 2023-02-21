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
 * 系统配置 sys_config
 */
@Data
@Table(name = "sys_config")
@EqualsAndHashCode(callSuper = true)
public class SysConfig extends BaseEntity<SysConfig> {

	/**
	 * 配置key
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "config_key")
	private String configKey;

	/**
	 * 配置的值
	 */
	@Column(name = "config_value")
	private String configValue;

	/**
	 * 配置描述
	 */
	@Column(name = "config_desc")
	private String configDesc;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
