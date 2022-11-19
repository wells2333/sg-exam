package com.github.tangyi.auth.security.wx;

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
 * 微信登录配置
 *
 * @author tangyi
 * @date 2019/07/05 19:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AuthenticationEventPublisher defaultAuthenticationEventPublisher;

	@Autowired
	private UserTokenService userTokenService;

	private CustomUserDetailsService userDetailsService;

	@Override
	public void configure(HttpSecurity http) {
		WxLoginFilter wxLoginFilter = new WxLoginFilter(userTokenService);
		wxLoginFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		wxLoginFilter.setEventPublisher(defaultAuthenticationEventPublisher);
		WxAuthenticationProvider wxAuthenticationProvider = new WxAuthenticationProvider();
		wxAuthenticationProvider.setCustomUserDetailsService(userDetailsService);
		http.authenticationProvider(wxAuthenticationProvider)
				.addFilterAfter(wxLoginFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
