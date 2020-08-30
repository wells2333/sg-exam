package com.github.tangyi.common.log.utils;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.utils.SysUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 日志工具类
 *
 * @author tangyi
 * @date 2019/3/13 00:01
 */
public class LogUtil {

	public static Log getLog() {
		HttpServletRequest request = ((ServletRequestAttributes) Objects
				.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		Log sysLog = new Log();
		sysLog.setCreator(Objects.requireNonNull(SysUtil.getUser()));
		sysLog.setType(CommonConstant.STATUS_NORMAL);
		sysLog.setIp(ServletUtil.getClientIP(request));
		sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
		sysLog.setMethod(request.getMethod());
		sysLog.setUserAgent(request.getHeader("user-agent"));
		sysLog.setParams(HttpUtil.toParams(request.getParameterMap()));
		sysLog.setServiceId(getServiceId());
		return sysLog;
	}

	/**
	 *
	 * @return String
	 */
	private static String getServiceId() {
		return "";
	}
}
