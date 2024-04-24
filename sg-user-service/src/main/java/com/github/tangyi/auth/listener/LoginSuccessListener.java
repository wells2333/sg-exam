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

package com.github.tangyi.auth.listener;

import com.github.tangyi.auth.security.core.event.CustomAuthenticationSuccessEvent;
import com.github.tangyi.common.enums.LoginTypeEnum;
import com.github.tangyi.common.model.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Component
public class LoginSuccessListener implements ApplicationListener<CustomAuthenticationSuccessEvent> {

	@Override
	public void onApplicationEvent(CustomAuthenticationSuccessEvent event) {
		UserDetails userDetails = event.getUserDetails();
		if (userDetails instanceof CustomUserDetails customUserDetails) {
			String tenantCode = customUserDetails.getTenantCode();
			String username = userDetails.getUsername();
			ServletRequestAttributes attr = currentRequestAttributes();
			String ip = "";
			if (attr != null) {
				ip = attr.getRequest().getRemoteAddr();
			}
			long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - customUserDetails.getStartNanoTime());
			LoginTypeEnum loginType = customUserDetails.getLoginType();
			log.info("Login success, userId: {}, username: {}, tenantCode: {}, loginType: {}, ip: {}, took: {}ms",
					customUserDetails.getId(), username, tenantCode, loginType.getType(), ip, took);
		}
	}

	private static ServletRequestAttributes currentRequestAttributes() {
		try {
			RequestAttributes attr = RequestContextHolder.currentRequestAttributes();
			if (!(attr instanceof ServletRequestAttributes)) {
				throw new IllegalStateException("current request is not a servlet request");
			}

			return (ServletRequestAttributes) attr;
		} catch (Exception e) {
			// do nothing
		}
		return null;
	}
}
