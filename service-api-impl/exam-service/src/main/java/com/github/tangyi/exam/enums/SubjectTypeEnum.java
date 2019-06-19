package com.github.tangyi.exam.enums;

/**
 * 题目类型枚举
 *
 * @author tangyi
 * @date 2019/6/16 16:22
 */
public enum SubjectTypeEnum {

    /**
     * 选择题
     */
    CHOICES(0),

    /**
     * 简答题
     */
    SHORT_ANSWER(1);

    SubjectTypeEnum(Integer value) {
        this.value = value;
    }

    private Integer value;

    /**
     * 根据类型返回具体的SubjectType
     *
     * @param type type
     * @return SubjectType
     */
    public static SubjectTypeEnum match(Integer type) {
        for (SubjectTypeEnum item : SubjectTypeEnum.values()) {
            if (item.value.equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
