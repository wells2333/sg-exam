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
