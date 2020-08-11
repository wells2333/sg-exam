package com.github.tangyi.api.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户授权类型
 *
 * @author tangyi
 * @date 2019/07/03 13:35
 */
@Getter
@AllArgsConstructor
public enum IdentityType {

	PASSWORD(1, "密码"), PHONE_NUMBER(2, "手机号"), EMAIL(3, "邮箱"), WE_CHAT(4, "微信"), QQ(5, "QQ");

	private Integer value;

	private String desc;

	/**
	 * 根据类型返回具体的IdentityType
	 *
	 * @param type type
	 * @return IdentityType
	 */
	public static IdentityType matchByType(Integer type) {
		for (IdentityType item : IdentityType.values()) {
			if (item.value.equals(type)) {
				return item;
			}
		}
		return IdentityType.PASSWORD;
	}

	/**
	 * 根据描述返回具体的IdentityType
	 *
	 * @param desc desc
	 * @return IdentityType
	 */
	public static IdentityType matchByDesc(String desc) {
		for (IdentityType item : IdentityType.values()) {
			if (item.desc.equals(desc)) {
				return item;
			}
		}
		return IdentityType.PASSWORD;
	}
}
