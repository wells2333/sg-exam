package com.github.tangyi.auth.security.core;

import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.RUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) {
		String msg = authException.getMessage();
		log.info("未授权统一处理");
		authException.printStackTrace();
		RUtil.out(res, R.error(msg));
	}
}
