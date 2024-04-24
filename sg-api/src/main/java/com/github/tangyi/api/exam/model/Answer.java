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

package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 答题
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_answer")
public class Answer extends BaseEntity<Answer> {

	/**
	 * 考试记录 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "exam_record_id")
	private Long examRecordId;

	/**
	 * 题目 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "subject_id")
	private Long subjectId;

	/**
	 * 题目类型
	 */
	private Integer type;

	/**
	 * 答案
	 */
	private String answer;

	/**
	 * 答题类型，0：正确，1：错误
	 */
	@Column(name = "answer_type")
	private Integer answerType;

	/**
	 * 得分
	 */
	private Double score;

	/**
	 * 批改状态
	 */
	@Column(name = "mark_status")
	private Integer markStatus;

	/**
	 * 批改人
	 */
	@Column(name = "mark_operator")
	private String markOperator;

	/**
	 * 开始时间
	 */
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@Column(name = "end_time")
	private Date endTime;

	/**
	 * 语音播放次数
	 */
	@Column(name = "speech_play_cnt")
	private Long speechPlayCnt;
}
