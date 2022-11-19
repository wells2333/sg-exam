package com.github.tangyi.api.exam.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 考试记录
 *
 * @author tangyi
 * @date 2018/11/8 21:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exam_examination_record")
public class ExaminationRecord extends BaseEntity<ExaminationRecord> {

	@NotBlank(message = "用户ID不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "user_id")
	private Long userId;

	@NotBlank(message = "考试类型不能为空")
	@Column(name = "type")
	private Integer type;

	@NotBlank(message = "考试ID不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "examination_id")
	private Long examinationId;

	/**
	 * 开始时间
	 */
	@Column(name = "start_time")
	@JSONField(format = DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = BaseEntity.TIMEZONE)
	private Date startTime;

	/**
	 * 结束时间
	 */
	@Column(name = "end_time")
	@JSONField(format = DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = BaseEntity.TIMEZONE)
	private Date endTime;

	/**
	 * 成绩
	 */
	@Column(name = "score")
	private Double score;

	/**
	 * 错误题目数量
	 */
	@Column(name = "in_correct_number")
	private Integer inCorrectNumber;

	/**
	 * 正确题目数量
	 */
	@Column(name = "correct_number")
	private Integer correctNumber;

	/**
	 * 提交状态 1-已提交 0-未提交
	 */
	@NotBlank(message = "状态不能为空")
	@Column(name = "submit_status")
	private Integer submitStatus;
}
