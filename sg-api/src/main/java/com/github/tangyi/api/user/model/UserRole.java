package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_user_role")
public class UserRole extends BaseEntity<UserRole> {

	@Column(name = "user_id")
    private Long userId;

	@Column(name = "role_id")
    private Long roleId;
}
