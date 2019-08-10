package com.github.tangyi.common.security.config;

import com.github.tangyi.common.security.mobile.MobileSecurityConfigurer;
import com.github.tangyi.common.security.properties.FilterIgnorePropertiesConfig;
import com.github.tangyi.common.security.wx.WxSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

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

    @Autowired
    public CustomResourceServerConfig(FilterIgnorePropertiesConfig filterIgnorePropertiesConfig, MobileSecurityConfigurer mobileSecurityConfigurer, WxSecurityConfigurer wxSecurityConfigurer) {
        this.filterIgnorePropertiesConfig = filterIgnorePropertiesConfig;
        this.mobileSecurityConfigurer = mobileSecurityConfigurer;
        this.wxSecurityConfigurer = wxSecurityConfigurer;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
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
}
