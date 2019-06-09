package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 错题
 *
 * @author tangyi
 * @date 2018/11/8 21:06
 */
@Data
public class IncorrectAnswer extends BaseEntity<IncorrectAnswer> {

    /**
     * 考生ID
     */
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    /**
     * 考试ID
     */
    @NotBlank(message = "考试ID不能为空")
    private String examinationId;

    /**
     * 考试记录ID
     */
    @NotBlank(message = "考试记录ID不能为空")
    private String examRecordId;

    /**
     * 题目ID
     */
    @NotBlank(message = "题目ID不能为空")
    private String subjectId;

    /**
     * 题目序号
     */
    @NotBlank(message = "题目序号不能为空")
    private String serialNumber;

    /**
     * 错误答案
     */
    @NotBlank(message = "错题内容不能为空")
    private String incorrectAnswer;
}
