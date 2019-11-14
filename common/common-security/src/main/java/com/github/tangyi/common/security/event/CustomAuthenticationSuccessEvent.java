package com.github.tangyi.common.security.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 登录成功事件
 *
 * @author tangyi
 * @date 2019-11-11 23:40
 */
@Data
public class CustomAuthenticationSuccessEvent extends ApplicationEvent {

	private UserDetails userDetails;

	public CustomAuthenticationSuccessEvent(Authentication authentication, UserDetails userDetails) {
		super(authentication);
		this.userDetails = userDetails;
	}
}
