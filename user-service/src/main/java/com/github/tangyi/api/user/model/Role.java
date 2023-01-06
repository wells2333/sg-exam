package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_role")
public class Role extends BaseEntity<Role> {

	@NotBlank(message = "角色名称不能为空")
	@Column(name = "role_name")
	private String roleName;

	@NotBlank(message = "角色标识不能为空")
	@Column(name = "role_code")
	private String roleCode;

	@Column(name = "role_desc")
	private String roleDesc;

	private Integer status;

	@Transient
	private String menuIds;

	/**
	 * 是否默认 0-否，1-是
	 */
	@Column(name = "is_default")
	private Integer isDefault;
}
