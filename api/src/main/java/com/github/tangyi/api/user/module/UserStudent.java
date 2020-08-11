package com.github.tangyi.api.user.module;

import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户学生关联关系，一个用户可以绑定多个学生
 *
 * @author tangyi
 * @date 2019/07/09 15:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserStudent extends BaseEntity<UserStudent> {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 关系类型
     */
    private Integer relationshipType;
}
