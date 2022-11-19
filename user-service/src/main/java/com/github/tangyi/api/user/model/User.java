package com.github.tangyi.api.user.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 用户基本信息
 *
 * @author tangyi
 * @date 2018-08-25 15:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_user")
public class User extends BaseEntity<User> {

	/**
	 * 姓名
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 电话
	 */
	@Pattern(regexp = "^\\d{11}$", message = "请输入11位手机号")
	@Column(name = "phone")
	private String phone;

	/**
	 * 头像id
	 */
	@Column(name = "avatar_id")
	private Long avatarId;

	/**
	 * 邮箱
	 */
	@Email(message = "邮箱格式不正确")
	@Column(name = "email")
	private String email;

	/**
	 * 性别
	 */
	@Column(name = "gender")
	private Integer gender;

	/**
	 * 出生日期
	 */
	@Column(name = "born")
	private Date born;

	/**
	 * 描述
	 */
	@Column(name = "user_desc")
	private String userDesc;

	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 部门id
	 */
	@Column(name = "dept_id")
	private Long deptId;

	/**
	 * 角色列表
	 */
	private List<Role> roleList;

	/**
	 * 角色
	 */
	private List<Long> role;

	/**
	 * 引导注册人
	 */
	@Column(name = "parent_uid")
	private Long parentUid;

	/**
	 * 乡/镇
	 */
	@Column(name = "street_id")
	private Long streetId;

	/**
	 * 县
	 */
	@Column(name = "county_id")
	private Long countyId;

	/**
	 * 城市
	 */
	@Column(name = "city_id")
	private Long cityId;

	/**
	 * 省份
	 */
	@Column(name = "province_id")
	private Long provinceId;

	/**
	 * 最近登录时间
	 */
	@Column(name = "login_time")
	@JSONField(format = DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = TIMEZONE)
	private Date loginTime;

	/**
	 * 用户归档时间
	 */
	@Column(name = "lock_time")
	@JSONField(format = DATE_FORMAT)
	@JsonFormat(pattern = BaseEntity.DATE_FORMAT, timezone = TIMEZONE)
	private Date lockTime;

	/**
	 * 微信
	 */
	@Column(name = "wechat")
	private String wechat;

	/**
	 * 家庭角色，参考UserStudentConstant
	 */
	@Column(name = "family_role")
	private Integer familyRole;

	/**
	 * 个性签名
	 */
	@Column(name = "signature")
	private String signature;
}
