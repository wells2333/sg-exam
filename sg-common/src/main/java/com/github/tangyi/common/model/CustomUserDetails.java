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

package com.github.tangyi.common.model;

import com.github.tangyi.common.enums.LoginTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CustomUserDetails extends User {

	@Serial
	private static final long serialVersionUID = 1L;

	private String tenantCode;
	private Long id;
	private String phone;
	private long startNanoTime;
	private LoginTypeEnum loginType;

	public CustomUserDetails(Long id, String username, Collection<? extends GrantedAuthority> authorities,
			String tenantCode) {
		this(id, username, "", authorities, tenantCode);
	}

	public CustomUserDetails(Long id, String username, String password,
			Collection<? extends GrantedAuthority> authorities, String tenantCode) {
		super(username, password, authorities);
		this.id = id;
		this.tenantCode = tenantCode;
	}

	public CustomUserDetails(String username, String password, boolean enabled,
			Collection<? extends GrantedAuthority> authorities, String tenantCode, Long id, String phone,
			long startNanoTime, LoginTypeEnum loginType) {
		super(username, password, enabled, true, true, true, authorities);
		this.tenantCode = tenantCode;
		this.id = id;
		this.phone = phone;
		this.startNanoTime = startNanoTime;
		this.loginType = loginType;
	}
}
