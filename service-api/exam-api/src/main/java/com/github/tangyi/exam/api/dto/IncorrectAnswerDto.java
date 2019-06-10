package com.github.tangyi.exam.api.dto;

import com.github.tangyi.exam.api.module.IncorrectAnswer;
import lombok.Data;

/**
 * 错题Dto
 *
 * @author tangyi
 * @date 2018/12/25 22:29
 */
@Data
public class IncorrectAnswerDto extends IncorrectAnswer {

    /**
     * 题目名称
     */
    private String subjectName;

    /**
     * 题目类型
     */
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
