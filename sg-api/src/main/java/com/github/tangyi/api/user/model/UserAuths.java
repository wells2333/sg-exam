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

package com.github.tangyi.api.user.model;

import com.github.tangyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_user_auths")
public class UserAuths extends BaseEntity<UserAuths> {

    /**
     * 用户 ID
     */
	@Column(name = "user_id")
    private Long userId;

    /**
     * 授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ
     */
	@Column(name = "identity_type")
    private Integer identityType;

    /**
     * 唯一标识，如用户名、手机号
     */
	@Column(name = "identifier")
    private String identifier;

    /**
     * 密码凭证，跟授权类型有关，如密码、第三方系统的 token 等
     */
	@Column(name = "credential")
    private String credential;

}
