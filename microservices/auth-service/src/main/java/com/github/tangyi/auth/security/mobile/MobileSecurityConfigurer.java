package com.github.tangyi.auth.security.mobile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tangyi.auth.security.core.CustomUserDetailsService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 手机登录配置
 *
 * @author tangyi
 * @date 2019/6/22 21:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MobileSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationEventPublisher defaultAuthenticationEventPublisher;

    private AuthenticationSuccessHandler mobileLoginSuccessHandler;

    private CustomUserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) {
        // 手机登录过滤器
        MobileAuthenticationFilter mobileAuthenticationFilter = new MobileAuthenticationFilter();
        mobileAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        mobileAuthenticationFilter.setAuthenticationSuccessHandler(mobileLoginSuccessHandler);
        mobileAuthenticationFilter.setEventPublisher(defaultAuthenticationEventPublisher);
        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        mobileAuthenticationProvider.setCustomUserDetailsService(userDetailsService);
        // 增加手机登录的过滤器
        http.authenticationProvider(mobileAuthenticationProvider).addFilterAfter(mobileAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
