package com.github.tangyi.auth.security.core.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 登录成功事件
 *
 * @author tangyi
 * @date 2019-11-11 23:40
 */
public class CustomAuthenticationSuccessEvent extends ApplicationEvent {

    private UserDetails userDetails;

    public CustomAuthenticationSuccessEvent(Authentication authentication, UserDetails userDetails) {
        super(authentication);
        this.userDetails = userDetails;
    }

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
}

