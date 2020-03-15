package com.github.tangyi.auth.config;

import com.github.tangyi.auth.security.CustomTokenConverter;
import com.github.tangyi.common.security.core.ClientDetailsServiceImpl;
import com.github.tangyi.common.security.exception.CustomOauthException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;

/**
 * 授权服务器配置
 *
 * @author tangyi
 * @date 2019-03-14 11:40
 */
@Configuration
public class CustomAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

	private AuthenticationConfiguration authenticationConfiguration;

	/**
     * redis连接工厂
     */
    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * 数据源
     */
    private final DataSource dataSource;

    /**
     * key配置信息
     */
    private final KeyProperties keyProperties;

    @Autowired
    public CustomAuthorizationServerConfigurer(RedisConnectionFactory redisConnectionFactory,
                                               DataSource dataSource,
                                               KeyProperties keyProperties) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.dataSource = dataSource;
        this.keyProperties = keyProperties;
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
	 * 生成KeyPair
	 * @return KeyPair
	 */
	@Bean
	public KeyPair keyPair() {
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyProperties.getKeyStore().getLocation(), keyProperties.getKeyStore().getPassword().toCharArray());
		return keyStoreKeyFactory.getKeyPair(keyProperties.getKeyStore().getAlias());
	}

    /**
     * token增强，使用非对称加密算法来对Token进行签名
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
		return new CustomTokenConverter(keyPair(), Collections.singletonMap("kid", "bael-key-id"));
    }

	@Bean
	public JWKSet jwkSet() {
		RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) keyPair().getPublic())
				.keyUse(KeyUse.SIGNATURE)
				.algorithm(JWSAlgorithm.RS256)
				.keyID("bael-key-id");
		return new JWKSet(builder.build());
	}

    /**
     * 使用自定义的JdbcClientDetailsService客户端详情服务
     *
     * @return ClientDetailsService
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new ClientDetailsServiceImpl(dataSource);
    }

    /**
     * 从数据库加载客户端信息
     *
     * @param clients clients
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    /**
     * 配置TokenStore、Token增强、认证管理器以及异常处理
     *
     * @param endpoints endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
				.authenticationManager(this.authenticationConfiguration.getAuthenticationManager())
                // 将token存储到redis
                .tokenStore(tokenStore())
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
        oauthServer
                .passwordEncoder(new BCryptPasswordEncoder())
                // 开启/oauth/token_key验证端口无权限访问
                .tokenKeyAccess("permitAll()")
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

	@Autowired
	public void setAuthenticationConfiguration(AuthenticationConfiguration authenticationConfiguration) {
		this.authenticationConfiguration = authenticationConfiguration;
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
                    return ResponseEntity.status(exception.getHttpErrorCode()).body(new CustomOauthException(e.getMessage()));
                } else {
                    throw e;
                }
            }
        };
    }
}

