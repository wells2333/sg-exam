package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 答题
 *
 * @author tangyi
 * @date 2018/11/8 20:59
 */
@Data
public class Answer extends BaseEntity<Answer> {

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    /**
     * 考试ID
     */
    @NotBlank(message = "考试ID不能为空")
    private String examinationId;

    /**
     * 考试记录id
     */
    @NotBlank(message = "考试记录ID不能为空")
    private String examRecordId;

    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 题目ID
     */
    @NotBlank(message = "题目ID不能为空")
    private String subjectId;

    /**
     * 答案
     */
    private String answer;

    /**
     * 选择题答案
     */
    private String optionAnswer;

    /**
     * 下一题题目序号
     */
    private String serialNumber;

}
