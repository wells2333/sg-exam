package com.github.tangyi.api.user.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * 部门
 *
 * @author tangyi
 * @date 2018/8/26 22:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_dept")
public class Dept extends BaseEntity<Dept> {

	/**
	 * 部门名称
	 */
	@NotBlank(message = "部门名称不能为空")
	@Column(name = "dept_name")
	private String deptName;

	/**
	 * 部门描述
	 */
	@Column(name = "dept_desc")
	private String deptDesc;

	/**
	 * 部门负责人
	 */
	@Column(name = "dept_leader")
	private String deptLeader;

	/**
	 * 父部门ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "parent_id")
	private Long parentId;

	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;
}
