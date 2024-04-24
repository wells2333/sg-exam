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

package com.github.tangyi.common.utils;

import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.security.JwtAuthenticationToken;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

@Slf4j
public class SysUtil {

    private static final String ADMIN_IDENTIFIER = EnvUtils.getValue("ADMIN_IDENTIFIER", "admin");

    private SysUtil() {
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isAdmin(String identifier) {
        return ADMIN_IDENTIFIER.equals(identifier);
    }

    public static String getUser() {
        Authentication authentication = getAuthentication();
        Preconditions.checkNotNull(authentication, "Authentication is null");
        return authentication.getName();
    }

    public static CustomUserDetails getUserDetails() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) getAuthentication();
            if (authenticationToken != null) {
                return (CustomUserDetails) authenticationToken.getPrincipal();
            }
        }
        return null;
    }

    public static Long getUserId() {
        CustomUserDetails details = getUserDetails();
        if (details != null) {
            return details.getId();
        }
        return null;
    }

    public static String getUserName() {
        CustomUserDetails details = getUserDetails();
        if (details != null) {
            return details.getUsername();
        }
        return "";
    }

    public static Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthentication().getAuthorities();
    }

    public static String getTenantCode() {
        String tenantCode = null;
        CustomUserDetails userDetails = getUserDetails();
        if (userDetails == null) {
            // 用户没有登录,这时可以调用默认租户
            tenantCode = TenantHolder.getTenantCode();
            if (StringUtils.isBlank(tenantCode))
                throw new IllegalArgumentException("tenant code is blank");
        } else
            tenantCode = userDetails.getTenantCode();
        return tenantCode;
    }
}
