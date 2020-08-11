package com.github.tangyi.api.exam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tangyi.common.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 考试记录DTO
 *
 * @author tangyi
 * @date 2018-12-26 16:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExaminationRecordDto extends BaseEntity<ExaminationRecordDto> {

    /**
     * 考生ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
     * 考试ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long examinationId;

    /**
     * 考试名称
     */
    private String examinationName;

    /**
     * 考试类型
     */
    private Integer type;

    /**
     * 考试注意事项
     */
    private String attention;

    /**
     * 当前时间
     */
    private Date currentTime;

    /**
     * 考试开始时间
     */
    private Date startTime;

    /**
     * 考试结束时间
     */
    private Date endTime;

    /**
     * 考试持续时间
     */
    private String duration;

    /**
     * 总分
     */
    private Integer totalScore;

    /**
     * 分数
     */
    private Double score;

    /**
     * 考试状态
     */
    private Integer status;

    /**
     * 封面
     */
    private String avatar;

    /**
     * 学院
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long collegeId;

    /**
     * 专业
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long majorId;

    /**
     * 课程
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long courseId;

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
    private Integer inCorrectNumber;

    /**
     * 正确题目数量
     */
    private Integer correctNumber;

    /**
     * 提交状态 1-已提交 0-未提交
     */
    private Integer submitStatus;

    private String submitStatusName;

	/**
	 * 答题列表
	 */
	private List<AnswerDto> answers;

}
