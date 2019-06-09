package com.github.tangyi.exam.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 题库
 *
 * @author tangyi
 * @date 2018/12/9 14:09
 */
@Data
public class SubjectBank extends BaseEntity<SubjectBank> {

    /**
     * 分类ID
     */
    @NotBlank(message = "分类ID不能为空")
    private String categoryId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String categoryName;

    /**
     * 题目序号
     */
    @NotBlank(message = "题目序号不能为空")
    private String serialNumber;

    /**
     * 题目名称
     */
    @NotBlank(message = "题目名称不能为空")
    private String subjectName;

    /**
     * 题目类型
     */
    @NotBlank(message = "题目类型不能为空")
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
     * 选项E
     */
    private String optionE;

    /**
     * 选项F
     */
    private String optionF;

    /**
     * 参考答案
     */
    private String answer;

    /**
     * 分值
     */
    @NotBlank(message = "题目分值不能为空")
    private String score;

    /**
     * 解析
     */
    private String analysis;

    /**
     * 难度等级
     */
    private String level;
}
