package com.github.tangyi.api.exam.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 考试权限
 *
 * @author tangyi
 * @date 2019-07-16 14:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExaminationAuth extends BaseEntity<ExaminationAuth> {

    /**
     * 学生ID
     */
    private String userId;

    /**
     * 考试ID
     */
    private String examinationId;
}
