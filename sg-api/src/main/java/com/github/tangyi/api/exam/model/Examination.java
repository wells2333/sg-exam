package com.github.tangyi.api.exam.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
	@JSONField(format = DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = TIMEZONE)
	private Date startTime;

	/**
	 * 考试结束时间
	 */
	@Column(name = "end_time")
	@JSONField(format = DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = TIMEZONE)
	private Date endTime;

	/**
	 * 总分
	 */
	@NotNull(message = "总分不能为空")
	@Column(name = "total_score")
	private Integer totalScore;

	/**
	 * 考试状态
	 */
	private Integer status;

	/**
	 * 封面对应的图片id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "image_id")
	private Long imageId;

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
	 * 答题模式，0：展示全部题目，1：上一题、下一题模式
	 */
	@Column(name = "answer_type")
	private Integer answerType;

	/**
	 * 权限控制，0：全部用户，1：指定用户，2：指定部门
	 */
	@Column(name = "access_type")
	private Integer accessType;

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
