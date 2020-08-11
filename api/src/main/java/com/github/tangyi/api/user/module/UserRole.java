package com.github.tangyi.api.user.module;

import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色关系
 *
 * @author tangyi
 * @date 2018/8/26 09:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRole extends BaseEntity<UserRole> {

    private Long userId;

    private Long roleId;
}
