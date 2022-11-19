package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户角色关系
 *
 * @author tangyi
 * @date 2018/8/26 09:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_user_role")
public class UserRole extends BaseEntity<UserRole> {

	@Column(name = "user_id")
    private Long userId;

	@Column(name = "role_id")
    private Long roleId;
}
