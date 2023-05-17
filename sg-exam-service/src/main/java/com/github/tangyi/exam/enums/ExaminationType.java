package com.github.tangyi.exam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExaminationType {

    FORMAL("考试", 0),

    PRACTICE("练习", 1),

    QUESTIONNAIRE("问卷", 2),

	INTERVIEW("面试", 3);

    private String name;

    private Integer value;

    public static ExaminationType matchByValue(Integer value) {
        for (ExaminationType item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return FORMAL;
    }

    public static ExaminationType matchByName(String name) {
        for (ExaminationType item : values()) {
            if (item.name.equals(name)) {
                return item;
            }
        }
        return FORMAL;
    }
}
