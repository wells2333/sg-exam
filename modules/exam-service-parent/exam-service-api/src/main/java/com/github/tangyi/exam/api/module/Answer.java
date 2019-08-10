package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 答题
 *
 * @author tangyi
 * @date 2018/11/8 20:59
 */
@Data
public class Answer extends BaseEntity<Answer> {

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
     * 批改状态
     */
    private Integer markStatus;
}
