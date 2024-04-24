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

package com.github.tangyi.api.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DashboardDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 在线用户数量
     */
    private String onlineUserNumber;

    /**
     * 考试数量
     */
    private String examinationNumber;

	/**
	 * 考试记录数量
	 */
	private String examinationRecordNumber;


	/**
     * 参与人数
     */
    private String examUserNumber;

	/**
	 * 单位数量
	 */
	private String tenantCount;

	/**
	 * 课程数
	 */
	private String courseCount;

	/**
	 * 考试记录数量
	 */
	private List<String> examRecordData = new ArrayList<>();

	/**
	 * 考试记录日期
	 */
	private List<String> examRecordDate = new ArrayList<>();
}
