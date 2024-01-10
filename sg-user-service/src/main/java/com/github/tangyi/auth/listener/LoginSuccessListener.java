package com.github.tangyi.auth.listener;

import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.auth.security.core.event.CustomAuthenticationSuccessEvent;
import com.github.tangyi.common.constant.CommonConstant;
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
		if (userDetails instanceof CustomUserDetails customUserDetails) {
			String tenantCode = customUserDetails.getTenantCode();
			String username = userDetails.getUsername();
			log.info("Login success, username: {} , tenantCode: {}", username, tenantCode);
			// 记录日志
			Log info = new Log();
			info.setTitle("用户登录");
			info.setCommonValue(username, tenantCode);
			info.setTook(String.valueOf(
					TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - customUserDetails.getStartNanoTime())));
			info.setType(CommonConstant.STATUS_NORMAL);
			ServletRequestAttributes attr = currentRequestAttributes();
			if (attr != null) {
				HttpServletRequest req = attr.getRequest();
				info.setMethod(req.getMethod());
				info.setRequestUri(req.getRequestURI());
				info.setIp(req.getRemoteAddr());
				info.setUserAgent(req.getHeader(HttpHeaders.USER_AGENT));
			}
			info.setServiceId("user-service");
			UserDto dto = new UserDto(null);
			dto.setId(customUserDetails.getId());
			dto.setIdentifier(username);
			dto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
			dto.setTenantCode(tenantCode);
			saveLoginInfo(info, dto);
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
			RequestAttributes attr = RequestContextHolder.currentRequestAttributes();
			if (!(attr instanceof ServletRequestAttributes)) {
				throw new IllegalStateException("current request is not a servlet request");
			}

			return (ServletRequestAttributes) attr;
		} catch (Exception e) {
			// do nothing
		}
		return null;
	}
}
