package com.github.tangyi.api.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangyi
 * @date 2019/07/10 18:07
 */
@Data
public class StudentDto implements Serializable {

	private static final long serialVersionUID = 2602249526687821147L;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;

	/**
	 * 学生id
	 */
	@Schema(description = "学生id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long studentId;

	/**
	 * 关系类型
	 */
	@Schema(description = "关系类型")
	private Integer relationshipType;

	/**
	 * 学生姓名
	 */
	@Schema(description = "学生姓名")
	private String studentName;

	/**
	 * 出生日期
	 */
	@Schema(description = "出生日期")
	private Date born;

	/**
	 * 电话号码
	 */
	@Schema(description = "电话", example = "15521089184")
	private String phone;

	/**
	 * 微信
	 */
	@Schema(description = "微信")
	private String wechat;

	/**
	 * 性别
	 */
	@Schema(description = "性别，0：男，1：女", example = "0")
	private Integer gender;

	/**
	 * 家庭住址
	 */
	@Schema(description = "家庭住址")
	private String address;

	/**
	 * 就读年级
	 */
	@Schema(description = "就读年级ID")
	private String grade;


	/**
	 * 就读年级
	 */
	@Schema(description = "就读年级名称")
	private String gradeName;

	/**
	 * 就读学校
	 */
	@Schema(description = "就读学校")
	private String school;

	/**
	 * 就读城市
	 */
	@Schema(description = "就读城市ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long cityId;

	/**
	 * 县id
	 */
	@Schema(description = "就读区县ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long countyId;
}