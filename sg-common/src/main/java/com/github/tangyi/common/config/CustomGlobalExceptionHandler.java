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

package com.github.tangyi.common.config;

import com.github.tangyi.common.constant.ApiMsg;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.model.R;
import com.github.tangyi.common.utils.JsonMapper;
import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomGlobalExceptionHandler {

	public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

	public static void throwInternalServerError() {
		throw new CommonException(INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> validationBodyException(MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status) {
		log.error("[validationBodyException]", ex);
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
		return new ResponseEntity<>(new R<>(errors, ApiMsg.KEY_ERROR, ex.getMessage()), headers, status);
	}

	@ExceptionHandler(HttpMessageConversionException.class)
	public ResponseEntity<R<String>> parameterTypeException(HttpMessageConversionException e) {
		log.error("[parameterTypeException]", e);
		return new ResponseEntity<>(R.error(e.getMessage()), HttpStatus.OK);
	}

	@ExceptionHandler(CommonException.class)
	public ResponseEntity<R<String>> handleCommonException(Exception e) {
		log.error("[handleCommonException]", e);
		return new ResponseEntity<>(R.error(e.getMessage()), HttpStatus.OK);
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<Object> validExceptionHandler(BindException e) {
		log.error("[validExceptionHandler]", e);
		return new ResponseEntity<>(R.error(parseBindingResult(e.getBindingResult()).getMessage()), HttpStatus.OK);
	}

	@ExceptionHandler({BadSqlGrammarException.class, SQLSyntaxErrorException.class, SQLException.class})
	public ResponseEntity<Object> handleSQLException(SQLException e) {
		log.error("[handleSQLException]", e);
		return new ResponseEntity<>(R.error(e.getMessage()), HttpStatus.OK);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<R<String>> handleIOException(Exception e) {
		log.error("[handleIOException]", e);
		return new ResponseEntity<>(R.error(INTERNAL_SERVER_ERROR), HttpStatus.OK);
	}

	@ExceptionHandler(QiniuException.class)
	public ResponseEntity<R<String>> handleQiniuException(QiniuException e) {
		log.error("[handleQiNiuException]", e);
		R<String> responseBean = new R<>(null, ApiMsg.KEY_ERROR, INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(responseBean, HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<R<String>> handleException(Exception e) {
		log.error("[handleException]", e);
		return new ResponseEntity<>(R.error(INTERNAL_SERVER_ERROR), HttpStatus.OK);
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<R<String>> handleThrowable(Exception e) {
		log.error("[handleThrowable]", e);
		return new ResponseEntity<>(R.error(INTERNAL_SERVER_ERROR), HttpStatus.OK);
	}

	private Exception parseBindingResult(BindingResult bindingResult) {
		Map<String, String> map = new HashMap<>();
		for (FieldError e : bindingResult.getFieldErrors()) {
			map.put(e.getField(), e.getDefaultMessage());
		}
		return new CommonException(
				map.isEmpty() ? String.valueOf(ApiMsg.KEY_PARAM_VALIDATE) : JsonMapper.toJsonString(map));
	}
}
