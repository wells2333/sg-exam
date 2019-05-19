package com.github.tangyi.exam.api.dto;

import com.github.tangyi.common.core.persistence.BaseEntity;
import com.github.tangyi.exam.api.module.Answer;
import lombok.Data;

/**
 * @author tangyi
 * @date 2019/1/9 20:58
 */
@Data
public class SubjectDto extends BaseEntity<SubjectDto> {

    /**
     * 考试ID
     */
    private String examinationId;

    /**
     * 考试记录ID
     */
    private String examinationRecordId;

    /**
     * 题目序号
     */
    private String serialNumber;

    /**
     * 题目名称
     */
    private String subjectName;

    /**
     * 题目类型
     */
    private String type;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 选项A
     */
    private String optionA;

    /**
     * 选项B
     */
    private String optionB;

    /**
     * 选项C
     */
    private String optionC;

    /**
     * 选项D
     */
    private String optionD;

    /**
     * 分值
     */
    private String score;

    /**
     * 难度等级
     */
    private String level;

    /**
     * 答题
     */
    private Answer answer;
}
