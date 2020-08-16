package com.github.tangyi.common.security.config;

import com.github.tangyi.common.properties.FilterIgnorePropertiesConfig;
import com.github.tangyi.common.properties.SysProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 资源服务器配置
 *
 * @author tangyi
 * @date 2019-03-15 13:30
 */
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	private final SysProperties sysProperties;

	/**
	 * 开放权限的URL
	 */
	private final FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

	public CustomWebSecurityConfigurerAdapter(FilterIgnorePropertiesConfig filterIgnorePropertiesConfig,
			SysProperties sysProperties) {
		this.filterIgnorePropertiesConfig = filterIgnorePropertiesConfig;
		this.sysProperties = sysProperties;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String[] ignores = new String[filterIgnorePropertiesConfig.getUrls().size()];
		http.csrf().disable().httpBasic().disable().authorizeRequests().antMatchers("/actuator/**").permitAll()
				.antMatchers(filterIgnorePropertiesConfig.getUrls().toArray(ignores)).permitAll().anyRequest()
				.authenticated().and().oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt.jwkSetUri(sysProperties.getAuthServiceUrl() + "/.well-known/jwks.json")));
	}
}
