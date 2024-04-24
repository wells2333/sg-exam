/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.api.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户授权类型
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
