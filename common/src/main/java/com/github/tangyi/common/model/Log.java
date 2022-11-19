package com.github.tangyi.common.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * 日志
 *
 * @author tangyi
 * @date 2018/10/31 20:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_log")
public class Log extends BaseEntity<Log> {

    /**
     * 日志类型
     */
	@Column(name = "type")
    private Integer type;

    /**
     * 日志标题
     */
    @NotBlank(message = "日志标题不能为空")
	@Column(name = "title")
    private String title;

    /**
     * 操作用户的IP地址
     */
	@Column(name = "ip")
    private String ip;

    /**
     * 操作用户代理信息
     */
	@Column(name = "user_agent")
    private String userAgent;

    /**
     * 操作的URI
     */
	@Column(name = "request_uri")
    private String requestUri;

    /**
     * 操作的方式
     */
	@Column(name = "method")
    private String method;

    /**
     * 操作提交的数据
     */
	@Column(name = "params")
    private String params;

    /**
     * 异常信息
     */
	@Column(name = "exception")
    private String exception;

    /**
     * 服务ID
     */
	@Column(name = "service_id")
    private String serviceId;

    /**
     * 耗时
     */
	@Column(name = "took")
    private String took;
}
