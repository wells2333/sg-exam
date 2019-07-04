package com.github.tangyi.user.api.enums;

/**
 * 用户授权类型
 *
 * @author tangyi
 * @date 2019/07/03 13:35
 */
public enum IdentityType {

    PASSWORD(1, "密码"),
    PHONE_NUMBER(2, "手机号"),
    EMAIL(3, "邮箱"),
    WE_CHAT(4, "微信"),
    QQ(5, "QQ");

    IdentityType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private Integer value;

    private String desc;

    /**
     * 根据类型返回具体的IdentityType
     *
     * @param type type
     * @return IdentityType
     */
    public static IdentityType match(Integer type) {
        for (IdentityType item : IdentityType.values()) {
            if (item.value.equals(type)) {
                return item;
            }
        }
        return IdentityType.PASSWORD;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
