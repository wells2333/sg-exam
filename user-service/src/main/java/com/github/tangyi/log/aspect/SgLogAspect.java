package com.github.tangyi.log.aspect;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.log.annotation.SgLog;
import com.github.tangyi.log.event.SgLogEvent;
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

@Aspect
@Configuration
public class SgLogAspect {

	private static final Logger logger = LoggerFactory.getLogger(SgLogAspect.class);

	@Around("@annotation(log)")
	public Object around(ProceedingJoinPoint point, SgLog log) throws Throwable {
		String strClassName = point.getTarget().getClass().getName();
		String strMethodName = point.getSignature().getName();
		String user = SysUtil.getUser();
		logger.info("[sg-log] {}.{}, operator: {}, type: {}", strClassName, strMethodName, user, log.operationType());
		com.github.tangyi.common.model.Log logVo = getLog(user);
		logVo.setTitle(log.value());
		// 发送异步日志事件
		Long startTime = System.currentTimeMillis();
		Object obj = point.proceed();
		Long endTime = System.currentTimeMillis();
		logVo.setTook(String.valueOf(endTime - startTime));
		logVo.setCommonValue(user, SysUtil.getTenantCode());
		SpringContextHolder.publishEvent(new SgLogEvent(logVo));
		return obj;
	}

	public Log getLog(String user) {
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
				RequestContextHolder.getRequestAttributes())).getRequest();
		Log sysLog = new Log();
		sysLog.setCreator(user);
		sysLog.setOperator(user);
		sysLog.setType(CommonConstant.STATUS_NORMAL);
		sysLog.setIp(ServletUtil.getClientIP(request));
		sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
		sysLog.setMethod(request.getMethod());
		sysLog.setUserAgent(request.getHeader("user-agent"));
		sysLog.setParams(HttpUtil.toParams(request.getParameterMap()));
		sysLog.setServiceId("user-service");
		return sysLog;
	}
}