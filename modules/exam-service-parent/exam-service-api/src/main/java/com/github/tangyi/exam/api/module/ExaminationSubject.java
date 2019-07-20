package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 考试题目关联
 *
 * @author tangyi
 * @date 2019/6/16 13:46
 */
@Data
public class ExaminationSubject extends BaseEntity<ExaminationSubject> {

    /**
     * 考试ID
     */
    private String examinationId;

    /**
     * 题目ID
     */
    private String subjectId;

    /**
     * 题目序号
     */
    private Integer serialNumber;

    /**
     * 题目类型，0：选择题，1：简答题，2：判断题，3：多选题
     */
    private Integer type;
}
