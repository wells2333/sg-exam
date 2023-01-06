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
 * 用户收藏 exam_user_favorites
 */
@Data
@Table(name = "exam_user_favorites")
@EqualsAndHashCode(callSuper = true)
public class ExamUserFav extends BaseEntity<ExamUserFav> {

	/**
	 * 目标ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "target_id")
	private Long targetId;

	/**
	 * 用户ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 类型ID，0：考试，1：题目，2：课程
	 */
	@Column(name = "target_type")
	private Integer targetType;

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setTargetType(Integer targetType) {
		this.targetType = targetType;
	}

	public Integer getTargetType() {
		return targetType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
