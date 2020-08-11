package com.github.tangyi.auth.config.security;

import com.github.tangyi.auth.exceptions.CustomOauthException;
import com.github.tangyi.auth.security.core.CustomTokenConverter;
import com.github.tangyi.auth.security.core.CustomTokenServices;
import com.github.tangyi.auth.security.core.CustomUserDetailsByNameServiceWrapper;
import com.github.tangyi.auth.security.core.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.security.KeyPair;
import java.util.Collections;

/**
 * 授权服务器配置
 *
 * @author tangyi
 * @date 2019-03-14 11:40
 */
@EnableAuthorizationServer
@Configuration
public class CustomAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

	private AuthenticationConfiguration authenticationConfiguration;

	private final RedisConnectionFactory redisConnectionFactory;

	private final CustomUserDetailsService userDetailsService;

	private final KeyPair keyPair;

	private final BCryptPasswordEncoder encoder;

	@Autowired
	public CustomAuthorizationServerConfigurer(AuthenticationConfiguration authenticationConfiguration,
			RedisConnectionFactory redisConnectionFactory, CustomUserDetailsService userDetailsService, KeyPair keyPair,
			BCryptPasswordEncoder encoder) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.redisConnectionFactory = redisConnectionFactory;
		this.userDetailsService = userDetailsService;
		this.keyPair = keyPair;
		this.encoder = encoder;
	}

	/**
	 * 将token存储到redis
	 *
	 * @return TokenStore
	 */
	@Bean
	public TokenStore tokenStore() {
		return new RedisTokenStore(redisConnectionFactory);
	}

	/**
	 * token增强，使用非对称加密算法来对Token进行签名
	 *
	 * @return JwtAccessTokenConverter
	 */
	@Bean
	protected JwtAccessTokenConverter jwtTokenEnhancer() {
		return new CustomTokenConverter(keyPair, Collections.emptyMap());
	}

	/**
	 * 防止刷新token是调用默认的loadUserByUsername，需要自定义tokenService
	 * @param endpoints endpoints
	 * @return CustomTokenServices
	 */
	private CustomTokenServices tokenService(AuthorizationServerEndpointsConfigurer endpoints) {
		CustomTokenServices tokenServices = new CustomTokenServices();
		tokenServices.setTokenStore(tokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setReuseRefreshToken(true);
		tokenServices.setAccessTokenValiditySeconds(-1);
		tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
		tokenServices.setTokenEnhancer(jwtTokenEnhancer());
		addUserDetailsService(tokenServices, userDetailsService);
		return tokenServices;
	}

	/**
	 * PreAuthenticatedAuthenticationProvider自定义userDetailsService
	 * @param tokenServices tokenServices
	 * @param userDetailsService userDetailsService
	 */
	private void addUserDetailsService(CustomTokenServices tokenServices, CustomUserDetailsService userDetailsService) {
		if (userDetailsService != null) {
			PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
			provider.setPreAuthenticatedUserDetailsService(
					new CustomUserDetailsByNameServiceWrapper<>(userDetailsService));
			tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
		}
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("web_app")
				.authorizedGrantTypes("code", "authorization_code", "implicit", "password")
				.redirectUris("http://my.redirect.uri").secret(encoder.encode("secret")).scopes("read", "write")
				.accessTokenValiditySeconds(600_000_000);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
				.authenticationManager(this.authenticationConfiguration.getAuthenticationManager())
				// 将token存储到redis
				.tokenStore(tokenStore()).tokenServices(tokenService(endpoints))
				// token增强
				.tokenEnhancer(jwtTokenEnhancer())
				// 异常转换
				.exceptionTranslator(webResponseExceptionTranslator());
	}

	/**
	 * 配置认证规则，哪些需要认证哪些不需要
	 *
	 * @param oauthServer oauthServer
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		oauthServer.passwordEncoder(new BCryptPasswordEncoder())
				// 开启/oauth/token_key验证端口无权限访问
				.tokenKeyAccess("permitAll()")
				// 开启/oauth/check_token验证端口认证权限访问
				.checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
	}

	@Bean
	@Lazy
	public WebResponseExceptionTranslator<OAuth2Exception> webResponseExceptionTranslator() {
		return new DefaultWebResponseExceptionTranslator() {
			@Override
			public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
				if (e instanceof OAuth2Exception) {
					OAuth2Exception exception = (OAuth2Exception) e;
					// 转换返回结果
					return ResponseEntity.status(exception.getHttpErrorCode())
							.body(new CustomOauthException(e.getMessage()));
				} else {
					throw e;
				}
			}
		};
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(this.keyPair);
		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		accessTokenConverter.setUserTokenConverter(new SubjectAttributeUserTokenConverter());
		converter.setAccessTokenConverter(accessTokenConverter);
		return converter;
	}
}
