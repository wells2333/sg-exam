package com.github.tangyi.api.exam.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 考试权限
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExaminationAuth extends BaseEntity<ExaminationAuth> {

    /**
     * 学生 ID
     */
    private String userId;

    /**
     * 考试 ID
     */
    private String examinationId;
}
