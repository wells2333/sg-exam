package com.github.tangyi.api.exam.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExaminationRecordDto extends BaseEntity<ExaminationRecordDto> {

	/**
	 * 考生ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;

	/**
	 * 考试ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long examinationId;

	/**
	 * 考试名称
	 */
	private String examinationName;

	/**
	 * 考试类型
	 */
	private Integer type;

	/**
	 * 考试类型名称
	 */
	private String typeLabel;

	/**
	 * 考试注意事项
	 */
	private String attention;

	/**
	 * 当前时间
	 */
	@JSONField(format = DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = BaseEntity.TIMEZONE)
	private Date currentTime;

	/**
	 * 考试开始时间
	 */
	@JSONField(format = DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = BaseEntity.TIMEZONE)
	private Date startTime;

	/**
	 * 考试结束时间
	 */
	@JSONField(format = DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = BaseEntity.TIMEZONE)
	private Date endTime;

	/**
	 * 考试持续时间
	 */
	private String duration;

	/**
	 * 总分
	 */
	private Integer totalScore;

	/**
	 * 分数
	 */
	private Double score;

	/**
	 * 考试状态
	 */
	private Integer status;

	/**
	 * 封面
	 */
	private String avatar;

	/**
	 * 学院
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long collegeId;

	/**
	 * 专业
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long majorId;

	/**
	 * 课程
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long courseId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 考生名称
	 */
	private String userName;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 考试时间
	 */
	private String examTime;

	/**
	 * 错误题目数量
	 */
	private Integer inCorrectNumber;

	/**
	 * 正确题目数量
	 */
	private Integer correctNumber;

	/**
	 * 提交状态 1-已提交 0-未提交
	 */
	private Integer submitStatus;

	private String submitStatusName;

	/**
	 * 答题列表
	 */
	private List<AnswerDto> answers;

}
