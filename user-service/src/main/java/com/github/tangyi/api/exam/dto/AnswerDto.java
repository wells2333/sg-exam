package com.github.tangyi.api.exam.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangyi
 * @date 2019/6/18 15:02
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 考试记录id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long examRecordId;

	/**
	 * 题目ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
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
	private Integer answerType;

	/**
	 * 得分
	 */
	private Double score;

	/**
	 * 用户ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 题目详情
	 */
	private SubjectDto subject;

	/**
	 * 批改状态
	 */
	private Integer markStatus;

	/**
	 * 批改人
	 */
	private String markOperator;

	/**
	 * 开始时间
	 */
	@JSONField(format = BaseEntity.DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = BaseEntity.TIMEZONE)
	private Date startTime;

	/**
	 * 结束时间
	 */
	@JSONField(format = BaseEntity.DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = BaseEntity.TIMEZONE)
	private Date endTime;

	/**
	 * 耗时
	 */
	private String duration;
}
