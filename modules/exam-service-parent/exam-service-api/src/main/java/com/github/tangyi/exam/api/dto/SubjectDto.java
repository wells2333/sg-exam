package com.github.tangyi.exam.api.dto;

import com.github.tangyi.common.core.persistence.BaseEntity;
import com.github.tangyi.exam.api.module.Answer;
import com.github.tangyi.exam.api.module.SubjectOption;
import lombok.Data;

import java.util.List;

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
     * 题目分类ID
     */
    private String categoryId;

    /**
     * 题目序号
     */
    private Integer serialNumber;

    /**
     * 题目名称
     */
    private String subjectName;

    /**
     * 题目类型
     */
    private Integer type;

    /**
     * 选择题类型
     */
    private Integer choicesType;

    /**
     * 分值
     */
    private Integer score;


    /**
     * 解析
     */
    private String analysis;

    /**
     * 难度等级
     */
    private Integer level;

    /**
     * 答题
     */
    private Answer answer;

    /**
     * 选项列表
     */
    private List<SubjectOption> options;
}
