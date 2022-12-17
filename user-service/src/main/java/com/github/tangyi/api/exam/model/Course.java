package com.github.tangyi.api.exam.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

/**
 * 课程
 *
 * @author tangyi
 * @date 2018/11/8 20:43
 */
@Data
@Table(name = "exam_course")
@EqualsAndHashCode(callSuper = true)
public class Course extends BaseEntity<Course> {

	/**
	 * 课程名称
	 */
	@NotBlank(message = "课程名称不能为空")
	@Column(name = "course_name")
	private String courseName;

	/**
	 * 学院
	 */
	private String college;

	/**
	 * 专业
	 */
	private String major;

	/**
	 * 老师
	 */
	private String teacher;

	/**
	 * 课程描述
	 */
	@Column(name = "course_description")
	private String courseDescription;

	/**
	 * logoId
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "image_id")
	private Long imageId;

	/**
	 * image URL
	 */
	@Transient
	private String imageUrl;

	/**
	 * 收费类型：0：免费，1：收费，默认免费
	 */
	@Column(name = "charge_type")
	private Integer chargeType;

	/**
	 * 收费价格
	 */
	@Column(name = "charge_price")
	private Double chargePrice;

	/**
	 * 难度等级，1~5，默认3
	 */
	@Column(name = "level")
	private Integer level;

	/**
	 * 简短版描述
	 */
	@Column(name = "simple_desc")
	private String simpleDesc;

	/**
	 * 排序号
	 */
	@Column(name = "sort")
	private int sort;

	/**
	 * 课程状态，0：上架，1：下架
	 */
	@Column(name = "course_status")
	private int courseStatus;

	/**
	 * 报名人数
	 */
	@Transient
	private Integer memberCount;

	/**
	 * 是否收藏
	 */
	@Transient
	private boolean favorite;

	/**
	 * 收藏人数
	 */
	@Transient
	private Long favCount;
}
