package com.github.tangyi.auth.config.security;

import com.github.tangyi.auth.error.CustomOAuth2AccessDeniedHandler;
import com.github.tangyi.auth.filter.DecodePasswordFilter;
import com.github.tangyi.auth.filter.ValidateCodeFilter;
import com.github.tangyi.auth.security.core.CustomUserDetailsAuthenticationProvider;
import com.github.tangyi.auth.security.core.CustomUserDetailsService;
import com.github.tangyi.auth.security.mobile.MobileSecurityConfigurer;
import com.github.tangyi.auth.security.wx.WxSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

/**
 * Spring Security配置
 *
 * @author tangyi
 * @date 2019-03-14 14:35
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomUserDetailsService userDetailsService;

	/**
	 * 手机登录配置
	 */
	private final MobileSecurityConfigurer mobileSecurityConfigurer;

	/**
	 * 微信登录配置
	 */
	private final WxSecurityConfigurer wxSecurityConfigurer;

	private final DecodePasswordFilter decodePasswordFilter;

	private final ValidateCodeFilter validateCodeFilter;

	public SecurityConfig(CustomUserDetailsService userDetailsService,
			MobileSecurityConfigurer mobileSecurityConfigurer, WxSecurityConfigurer wxSecurityConfigurer,
			DecodePasswordFilter decodePasswordFilter, ValidateCodeFilter validateCodeFilter) {
		this.userDetailsService = userDetailsService;
		this.mobileSecurityConfigurer = mobileSecurityConfigurer;
		this.wxSecurityConfigurer = wxSecurityConfigurer;
		this.decodePasswordFilter = decodePasswordFilter;
		this.validateCodeFilter = validateCodeFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(validateCodeFilter, WebAsyncManagerIntegrationFilter.class);
		http.addFilterBefore(decodePasswordFilter, WebAsyncManagerIntegrationFilter.class);
		http
				// 前后端分离，关闭csrf
				.csrf().disable().
				httpBasic().disable().authorizeRequests().antMatchers("/actuator/**").permitAll()
				.mvcMatchers("/.well-known/jwks.json").permitAll().anyRequest().authenticated();
		// accessDeniedHandler
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		// 手机号登录
		http.apply(mobileSecurityConfigurer);
		// 微信登录
		http.apply(wxSecurityConfigurer);
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authProvider());
	}

	/**
	 * 认证Provider，提供获取用户信息、认证、授权等功能
	 *
	 * @return AuthenticationProvider
	 */
	@Bean
	public AuthenticationProvider authProvider() {
		return new CustomUserDetailsAuthenticationProvider(encoder(), userDetailsService);
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomOAuth2AccessDeniedHandler();
	}
}
