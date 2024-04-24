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
