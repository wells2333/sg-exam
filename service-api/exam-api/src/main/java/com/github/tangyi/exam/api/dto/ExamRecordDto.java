package com.github.tangyi.exam.api.dto;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 考试记录DTO
 *
 * @author tangyi
 * @date 2018-12-26 16:26
 */
@Data
public class ExamRecordDto extends BaseEntity<ExamRecordDto> {

    /**
     * 考生ID
     */
    private String userId;

    /**
     * 考试名称
     */
    private String examinationName;

    /**
     * 考试类型
     */
    private String type;

    /**
     * 考试注意事项
     */
    private String attention;

    /**
     * 当前时间
     */
    private String currentTime;

    /**
     * 考试开始时间
     */
    private String startTime;

    /**
     * 考试结束时间
     */
    private String endTime;

    /**
     * 考试持续时间
     */
    private String duration;

    /**
     * 总分
     */
    private String totalScore;

    /**
     * 分数
     */
    private String score;

    /**
     * 考试状态
     */
    private String status;

    /**
     * 封面
     */
    private String avatar;

    /**
     * 学院
     */
    private String collegeId;

    /**
     * 专业
     */
    private String majorId;

    /**
     * 课程
     */
    private String courseId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 考生名称
     */
    private String userName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 考试时间
     */
    private String examTime;

    /**
     * 错误题目数量
     */
    private String inCorrectNumber;

    /**
     * 正确题目数量
     */
    private String correctNumber;

    /**
     * 提交状态 1-已提交 0-未提交
     */
    private String submitStatus;
}
