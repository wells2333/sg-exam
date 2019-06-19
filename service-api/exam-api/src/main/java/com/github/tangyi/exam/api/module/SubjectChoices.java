package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 选择题
 *
 * @author tangyi
 * @date 2018/11/8 20:53
 */
@Data
public class SubjectChoices extends BaseEntity<SubjectChoices> {

    /**
     * 题目分类ID
     */
    private String categoryId;

    /**
     * 题目序号
     */
    @NotBlank(message = "题目序号不能为空")
    private Integer serialNumber;

    /**
     * 题目名称
     */
    @NotBlank(message = "题目名称不能为空")
    private String subjectName;

    /**
     * 参考答案
     */
    private String answer;

    /**
     * 分值
     */
    @NotBlank(message = "题目分值不能为空")
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
     * 题目类型，0：单选，1：多选
     */
    @NotBlank(message = "题目类型不能为空")
    private Integer choicesType;

    /**
     * 选项列表
     */
    private List<SubjectOption> options;
}
