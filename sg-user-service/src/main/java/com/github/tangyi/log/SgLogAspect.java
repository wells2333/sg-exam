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

package com.github.tangyi.log;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.log.SgLog;
import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.log.SgLogEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect
@Configuration
public class SgLogAspect {

	private static final Logger logger = LoggerFactory.getLogger(SgLogAspect.class);

	@Around("@annotation(log)")
	public Object around(ProceedingJoinPoint point, SgLog log) throws Throwable {
		String className = point.getTarget().getClass().getName();
		String methodName = point.getSignature().getName();
		String user = SysUtil.getUser();
		String tenantCode = SysUtil.getTenantCode();
		logger.info("[sys-log] {}.{}, operator: {}, type: {}", className, methodName, user, log.operationType());
		Log vo = getLog(user);
		vo.setTitle(log.value());
		long start = System.nanoTime();
		Object obj = point.proceed();
		long end = System.nanoTime();
		vo.setTook(String.valueOf(TimeUnit.NANOSECONDS.toMillis(end - start)));
		vo.setCommonValue(user, tenantCode);
		SpringContextHolder.publishEvent(new SgLogEvent(vo));
		return obj;
	}

	private Log getLog(String user) {
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
				RequestContextHolder.getRequestAttributes())).getRequest();
		Log log = new Log();
		log.setCreator(user);
		log.setOperator(user);
		log.setType(CommonConstant.STATUS_NORMAL);
		log.setIp(ServletUtil.getClientIP(request));
		log.setRequestUri(URLUtil.getPath(request.getRequestURI()));
		log.setMethod(request.getMethod());
		log.setUserAgent(request.getHeader("user-agent"));
		log.setParams(HttpUtil.toParams(request.getParameterMap()));
		log.setServiceId("user-service");
		return log;
	}
}