package com.github.tangyi.exam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 考试类型
 */
@Getter
@AllArgsConstructor
public enum ExaminationTypeEnum {

    FORMAL("正式考试", 0),

    MOCK("模拟考试", 1),

    PRACTICE("练习", 2),

    QUESTIONNAIRE("调查问卷", 3);;

    private String name;

    private Integer value;

    /**
     * 根据类型返回具体的ExaminationTypeEnum
     *
     * @param value value
     * @return ExaminationTypeEnum
     */
    public static ExaminationTypeEnum matchByValue(Integer value) {
        for (ExaminationTypeEnum item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return MOCK;
    }

    /**
     * 根据描述返回具体的ExaminationTypeEnum
     *
     * @param name name
     * @return ExaminationTypeEnum
     */
    public static ExaminationTypeEnum matchByName(String name) {
        for (ExaminationTypeEnum item : values()) {
            if (item.name.equals(name)) {
                return item;
            }
        }
        return MOCK;
    }
}
