package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 考试成员 exam_examination_member
 */
@Data
@Table(name = "exam_examination_member")
@EqualsAndHashCode(callSuper = true)
public class ExamExaminationMember extends BaseEntity<ExamExaminationMember> {

	/**
	 * 类型，0：课程，1：考试
	 */
	@Column(name = "exam_type")
	private Integer examType;

	@Column(name = "exam_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long examId;

	/**
	 * 成员类型，0：全部用户，1：用户ID，2：部门
	 */
	@Column(name = "member_type")
	private Integer memberType;

	/**
	 * 成员ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "member_id")
	private Long memberId;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
