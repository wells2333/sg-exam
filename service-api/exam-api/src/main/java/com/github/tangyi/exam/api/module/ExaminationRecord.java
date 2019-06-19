package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 考试记录
 *
 * @author tangyi
 * @date 2018/11/8 21:05
 */
@Data
public class ExaminationRecord extends BaseEntity<ExaminationRecord> {

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
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 成绩
     */
    private Integer score;

    /**
     * 错误题目数量
     */
    private Integer inCorrectNumber;

    /**
     * 正确题目数量
     */
    private Integer correctNumber;

    /**
     * 提交状态 1-已提交 0-未提交
     */
    @NotBlank(message = "状态不能为空")
    private Integer submitStatus;
}
