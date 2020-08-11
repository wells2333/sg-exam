package com.github.tangyi.user.config;

import com.github.tangyi.common.properties.FilterIgnorePropertiesConfig;
import com.github.tangyi.common.properties.SysProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 资源服务器配置
 *
 * @author tangyi
 * @date 2019-03-15 11:37
 */
@Configuration
@EnableWebSecurity
public class CustomResourceServerConfig extends WebSecurityConfigurerAdapter {

	private final SysProperties sysProperties;

	/**
	 * 开放权限的URL
	 */
	private final FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

	@Autowired
	public CustomResourceServerConfig(FilterIgnorePropertiesConfig filterIgnorePropertiesConfig,
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
