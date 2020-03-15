package com.github.tangyi.common.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tangyi.common.security.handler.CustomAccessDeniedHandler;
import com.github.tangyi.common.security.mobile.MobileSecurityConfigurer;
import com.github.tangyi.common.core.properties.FilterIgnorePropertiesConfig;
import com.github.tangyi.common.security.wx.WxSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 资源服务器配置
 *
 * @author tangyi
 * @date 2019-03-15 11:37
 */
@Configuration
@EnableResourceServer
public class CustomResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "resource_id";

    /**
     * 开放权限的URL
     */
    private final FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

    /**
     * 手机登录配置
     */
    private final MobileSecurityConfigurer mobileSecurityConfigurer;

    /**
     * 微信登录配置
     */
    private final WxSecurityConfigurer wxSecurityConfigurer;

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomResourceServerConfig(FilterIgnorePropertiesConfig filterIgnorePropertiesConfig,
                                      MobileSecurityConfigurer mobileSecurityConfigurer,
                                      WxSecurityConfigurer wxSecurityConfigurer,
                                      ObjectMapper objectMapper) {
        this.filterIgnorePropertiesConfig = filterIgnorePropertiesConfig;
        this.mobileSecurityConfigurer = mobileSecurityConfigurer;
        this.wxSecurityConfigurer = wxSecurityConfigurer;
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
        resources.accessDeniedHandler(accessDeniedHandler());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] ignores = new String[filterIgnorePropertiesConfig.getUrls().size()];
        http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers(filterIgnorePropertiesConfig.getUrls().toArray(ignores)).permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
        // 手机号登录
        http.apply(mobileSecurityConfigurer);
        // 微信登录
        http.apply(wxSecurityConfigurer);
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(objectMapper);
    }
}
