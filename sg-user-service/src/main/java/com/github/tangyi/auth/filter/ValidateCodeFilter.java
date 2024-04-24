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

package com.github.tangyi.auth.filter;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.common.constant.Status;
import com.github.tangyi.common.exceptions.InvalidValidateCodeException;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.RUtil;
import com.github.tangyi.user.service.ValidateCodeService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

	private final ValidateCodeService validateCodeService;

	@Override
	protected void doFilterInternal(HttpServletRequest req, @NotNull HttpServletResponse res,
			@NotNull FilterChain chain) throws ServletException, IOException {
		// 忽略验证码
		if (Status.OPEN.equals(req.getParameter("ignoreCode"))) {
			chain.doFilter(req, res);
		} else {
			doCheckCode(req, res, chain);
		}
	}

	public void doCheckCode(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String uri = req.getRequestURI();
		// 密码登录、手机号登录、注册才校验验证码
		if (HttpMethod.POST.matches(req.getMethod()) && StrUtil.containsAnyIgnoreCase(uri, SecurityConstant.REGISTER,
				SecurityConstant.MOBILE_LOGIN_URL)) {
			try {
				checkCode(req);
				chain.doFilter(req, res);
			} catch (Exception e) {
				RUtil.out(res, R.error(e.getMessage()));
			}
		} else {
			chain.doFilter(req, res);
		}
	}

	private void checkCode(HttpServletRequest req) throws InvalidValidateCodeException {
		String code = req.getParameter("code");
		if (StrUtil.isBlank(code)) {
			throw new InvalidValidateCodeException("Please input valid code.");
		}

		// 获取随机码
		String randomStr = req.getParameter("randomStr");
		// 随机数为空，则获取手机号
		if (StrUtil.isBlank(randomStr)) {
			randomStr = req.getParameter("mobile");
		}
		validateCodeService.checkCode(code, randomStr);
	}
}
