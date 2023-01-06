package com.github.tangyi.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tangyi.common.constant.ApiMsg;
import com.github.tangyi.common.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class RUtil {

	private RUtil() {
	}

	public static boolean isSuccess(R<?> r) {
		return r != null && r.getCode() == ApiMsg.KEY_SUCCESS;
	}

	public static String getMessage(R<?> r) {
		return r == null ? "" : r.getMessage();
	}

	public static void out(HttpServletResponse response, Object result) {
		response.setStatus(HttpStatus.OK.value());
		write(response, result);
	}

	public static void out(ServletResponse response, Object result) {
		write(response, result);
	}

	public static void write(ServletResponse response, Object result) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("utf-8");
		try (PrintWriter writer = response.getWriter()) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(writer, result);
		} catch (IOException e) {
			log.error("write response failed", e);
		}
	}
}
