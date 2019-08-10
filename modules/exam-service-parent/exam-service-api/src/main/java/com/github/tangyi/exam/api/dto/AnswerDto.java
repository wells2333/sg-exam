package com.github.tangyi.exam.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tangyi
 * @date 2019/6/18 15:02
 */
@Data
public class AnswerDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 考试记录id
     */
    private String examRecordId;

    /**
     * 题目ID
     */
    private String subjectId;

    /**
     * 题目类型
     */
    private Integer type;

    /**
     * 答案
     */
    private String answer;

    /**
     * 答题类型，0：正确，1：错误
     */
    private Integer answerType;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 题目序号
     */
    private Integer serialNumber;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 题目详情
     */
    private SubjectDto subject;

    /**
     * 批改状态
     */
    private Integer markStatus;
}
