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
import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_examination")
public class Examination extends BaseEntity<Examination> {

	/**
	 * 考试名称
	 */
	@NotBlank(message = "考试名称不能为空")
	@Column(name = "examination_name")
	private String examinationName;

	/**
	 * 考试类型
	 */
	@NotNull(message = "考试类型不能为空")
	private Integer type;

	/**
	 * 考试注意事项
	 */
	private String attention;

	/**
	 * 考试开始时间
	 */
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * 考试时长，单位：分钟
	 */
	@Column(name = "exam_duration_minute")
	private Integer examDurationMinute;

	/**
	 * 总分
	 */
	@NotNull(message = "总分不能为空")
	@Column(name = "total_score")
	private Integer totalScore;

	/**
	 * 考试状态，1：发布，0：未发布
	 */
	private Integer status;

	/**
	 * 封面对应的图片 ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "image_id")
	private Long imageId;

	private String imageUrl;
	/**
	 * 课程
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "course_id")
	private Long courseId;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 排序号
	 */
	private String sort;

	/**
	 * 标签，多个用逗号分隔
	 */
	@Column(name = "tags")
	private String tags;

	/**
	 * 出题模式，0：顺序出题，1：随机出题
	 */
	@Column(name = "show_subject_type")
	private Integer showSubjectType;

	/**
	 * 答题模式，0：单页模式，1：顺序模式
	 */
	@Column(name = "answer_type")
	private Integer answerType;

	/**
	 * 权限控制，0：全部用户，1：指定用户，2：指定部门
	 */
	@Column(name = "access_type")
	private Integer accessType;

	/**
	 * 考试次数，默认 0，表示不限制
	 */
	@Column(name = "max_exam_cnt")
	private Integer maxExamCnt;

	/**
	 * 考试次数
	 */
	@Transient
	private Long startCount;

	/**
	 * 收藏次数
	 */
	@Transient
	private Long favoritesCount;

	/**
	 * 成员 ID
	 */
	@Transient
	private List<Long> members;

	/**
	 * 部门 ID
	 */
	@Transient
	private String deptMember;

	public static Examination of(ExaminationDto dto) {
		Examination examination = new Examination();
		BeanUtils.copyProperties(dto, examination);
		return examination;
	}
}
