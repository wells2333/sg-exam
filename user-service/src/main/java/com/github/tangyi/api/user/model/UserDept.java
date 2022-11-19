package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户部门关系
 *
 * @author tangyi
 * @date 2018/10/27 10:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDept extends BaseEntity<UserDept> {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 部门ID
     */
    private String deptId;
}
