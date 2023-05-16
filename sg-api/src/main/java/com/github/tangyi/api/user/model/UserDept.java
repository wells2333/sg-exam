package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDept extends BaseEntity<UserDept> {

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 部门 ID
     */
    private String deptId;
}
