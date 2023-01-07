package com.github.tangyi.common.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_log")
public class Log extends BaseEntity<Log> {

	@Column(name = "type")
    private Integer type;

    @NotBlank(message = "日志标题不能为空")
	@Column(name = "title")
    private String title;

	@Column(name = "ip")
    private String ip;

	@Column(name = "user_agent")
    private String userAgent;

	@Column(name = "request_uri")
    private String requestUri;

	@Column(name = "method")
    private String method;

	@Column(name = "params")
    private String params;

	@Column(name = "exception")
    private String exception;

	@Column(name = "service_id")
    private String serviceId;

	@Column(name = "took")
    private String took;
}
