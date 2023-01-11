package com.github.tangyi.auth.security.mobile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tangyi.auth.security.core.user.CustomUserDetailsService;
import com.github.tangyi.auth.service.UserTokenService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 手机登录配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MobileSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AuthenticationEventPublisher defaultAuthenticationEventPublisher;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private UserTokenService userTokenService;

	@Override
	public void configure(HttpSecurity http) {
		MobileLoginFilter mobileLoginFilter = new MobileLoginFilter(userTokenService);
		mobileLoginFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		mobileLoginFilter.setEventPublisher(defaultAuthenticationEventPublisher);
		MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
		mobileAuthenticationProvider.setCustomUserDetailsService(userDetailsService);
		http.authenticationProvider(mobileAuthenticationProvider)
				.addFilterAfter(mobileLoginFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
