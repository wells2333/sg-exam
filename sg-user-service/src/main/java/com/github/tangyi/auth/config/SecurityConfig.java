package com.github.tangyi.auth.config;

import com.github.tangyi.auth.filter.ValidateCodeFilter;
import com.github.tangyi.auth.security.core.UnauthorizedEntryPoint;
import com.github.tangyi.auth.security.core.filter.TokenAuthenticationFilter;
import com.github.tangyi.auth.security.core.filter.TokenLoginFilter;
import com.github.tangyi.auth.security.core.provider.CustomUserDetailsAuthenticationProvider;
import com.github.tangyi.auth.security.core.user.CustomUserDetailsService;
import com.github.tangyi.auth.security.mobile.MobileSecurityConfigurer;
import com.github.tangyi.auth.security.wx.WxSecurityConfigurer;
import com.github.tangyi.auth.service.UserTokenService;
import com.github.tangyi.common.properties.FilterIgnorePropertiesConfig;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.security.OncePerRequestTokenFilter;
import com.github.tangyi.common.security.TokenManager;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import java.util.Set;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;

	private final TokenManager tokenManager;

	private final UserTokenService userTokenService;

	private final SysProperties sysProperties;

	private final ValidateCodeFilter validateCodeFilter;

	private final FilterIgnorePropertiesConfig ignorePropertiesConfig;

	public SecurityConfig(CustomUserDetailsService userDetailsService, TokenManager tokenManager,
			UserTokenService userTokenService, SysProperties sysProperties,
			ValidateCodeFilter validateCodeFilter, FilterIgnorePropertiesConfig ignorePropertiesConfig) {
		this.userDetailsService = userDetailsService;
		this.tokenManager = tokenManager;
		this.userTokenService = userTokenService;
		this.sysProperties = sysProperties;
		this.validateCodeFilter = validateCodeFilter;
		this.ignorePropertiesConfig = ignorePropertiesConfig;
	}

	@Bean
	public AuthenticationManagerFactoryBean authenticationManagerFactoryBean() {
		return new AuthenticationManagerFactoryBean();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManagerFactoryBean authenticationManagerFactoryBean) throws Exception {
		// 验证码
		http.addFilterBefore(validateCodeFilter, WebAsyncManagerIntegrationFilter.class);
		Set<String> ignoreUrls = Sets.newHashSet(ignorePropertiesConfig.getUrls());
		http
				// 未登录处理
				.exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint())
				.and()
				.csrf().disable()
				.requestCache().disable()
				.sessionManagement().disable()
				.securityContext().securityContextRepository(new NullSecurityContextRepository())
				.and()
				.addFilterAfter(new OncePerRequestTokenFilter(tokenManager, ignorePropertiesConfig), UsernamePasswordAuthenticationFilter.class)
				.httpBasic().disable()
				.authorizeRequests()
				.antMatchers(ignoreUrls.toArray(String[]::new)).permitAll().anyRequest()
				.authenticated();
		log.info("ignore urls: {}", ignoreUrls);
		http.authenticationProvider(authProvider());
		AuthenticationManager authenticationManager = authenticationManagerFactoryBean.getObject();
		http.addFilter(new TokenLoginFilter(authenticationManager, userTokenService, sysProperties));
		http.addFilter(new TokenAuthenticationFilter(authenticationManager, tokenManager));
		// 手机号登录
		http.apply(mobileSecurityConfigurer(userDetailsService));
		// 微信登录
		http.apply(wxSecurityConfigurer(userDetailsService));
		http.httpBasic();
		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 认证Provider，提供获取用户信息、认证、授权等功能
	 */
	@Bean
	public AuthenticationProvider authProvider() {
		return new CustomUserDetailsAuthenticationProvider(encoder(), userDetailsService);
	}

	/**
	 * 配置手机号登录
	 * 采用懒加载是因为只有认证授权服务需要手机登录的相关配置
	 *
	 * @return MobileSecurityConfigurer
	 */
	@Bean
	public MobileSecurityConfigurer mobileSecurityConfigurer(@Lazy CustomUserDetailsService userDetailsService) {
		MobileSecurityConfigurer mobileSecurityConfigurer = new MobileSecurityConfigurer();
		//mobileSecurityConfigurer.setMobileLoginSuccessHandler(mobileLoginSuccessHandler(encoder, objectMapper));
		mobileSecurityConfigurer.setUserDetailsService(userDetailsService);
		return mobileSecurityConfigurer;
	}

	/**
	 * 手机登录成功后的处理
	 *
	 * @return AuthenticationSuccessHandler
	 */
	//    @Bean
	//    public AuthenticationSuccessHandler mobileLoginSuccessHandler(
	//			PasswordEncoder encoder, ObjectMapper objectMapper) {
	//        return MobileLoginSuccessHandler.builder()
	//                .objectMapper(objectMapper)
	//                .clientDetailsService(clientDetailsService)
	//                .passwordEncoder(encoder)
	//                .defaultAuthorizationServerTokenServices(defaultAuthorizationServerTokenServices).build();
	//    }

	/**
	 * 配置微信登录
	 *
	 * @return WxSecurityConfigurer
	 */
	@Bean
	public WxSecurityConfigurer wxSecurityConfigurer(@Lazy CustomUserDetailsService userDetailsService) {
		WxSecurityConfigurer wxSecurityConfigurer = new WxSecurityConfigurer();
		wxSecurityConfigurer.setUserDetailsService(userDetailsService);
		return wxSecurityConfigurer;
	}
}
