package com.github.tangyi.auth.security.core;

import com.github.tangyi.common.constant.ApiMsg;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.RUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未授权处理
 * @author tangyi
 * @date 2022/5/1 3:04 下午
 */
@Slf4j
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		String msg = authException.getMessage();
		log.info("未授权统一处理");
		authException.printStackTrace();
		RUtil.out(response, R.error(msg));
	}
}
