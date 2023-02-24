package com.github.tangyi.auth.listener;

import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.auth.security.core.event.CustomAuthenticationSuccessEvent;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.ServiceConstant;
import com.github.tangyi.common.model.CustomUserDetails;
import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.utils.DateUtils;
import com.github.tangyi.user.service.sys.LogService;
import com.github.tangyi.user.service.sys.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Component
public class LoginSuccessListener implements ApplicationListener<CustomAuthenticationSuccessEvent> {

	private final UserService userService;

	private final LogService logService;

	@Override
	public void onApplicationEvent(CustomAuthenticationSuccessEvent event) {
		UserDetails userDetails = event.getUserDetails();
		if (userDetails instanceof CustomUserDetails) {
			CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
			String tenantCode = customUserDetails.getTenantCode();
			String username = userDetails.getUsername();
			log.info("Login success, username: {} , tenantCode: {}", username, tenantCode);
			// 记录日志
			Log logInfo = new Log();
			logInfo.setTitle("用户登录");
			logInfo.setCommonValue(username, tenantCode);
			logInfo.setTook(String.valueOf(
					TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - customUserDetails.getStartNanoTime())));
			logInfo.setType(CommonConstant.STATUS_NORMAL);
			ServletRequestAttributes requestAttributes = currentRequestAttributes();
			if (requestAttributes != null) {
				HttpServletRequest request = requestAttributes.getRequest();
				logInfo.setMethod(request.getMethod());
				logInfo.setRequestUri(request.getRequestURI());
				logInfo.setIp(request.getRemoteAddr());
				logInfo.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
			}
			logInfo.setServiceId(ServiceConstant.USER_SERVICE);
			UserDto userDto = new UserDto(null);
			userDto.setId(customUserDetails.getId());
			userDto.setIdentifier(username);
			userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
			userDto.setTenantCode(tenantCode);
			saveLoginInfo(logInfo, userDto);
		}
	}

	@Async
	public void saveLoginInfo(Log logInfo, UserDto userDto) {
		try {
			logService.save(logInfo);
			userService.updateLoginInfo(userDto);
		} catch (Exception e) {
			log.error("Failed to save login info", e);
		}
	}

	private static ServletRequestAttributes currentRequestAttributes() {
		try {
			RequestAttributes requestAttr = RequestContextHolder.currentRequestAttributes();
			if (!(requestAttr instanceof ServletRequestAttributes)) {
				throw new IllegalStateException("current request is not a servlet request");
			}
			return (ServletRequestAttributes) requestAttr;
		} catch (Exception e) {
			// do nothing
		}
		return null;
	}
}
