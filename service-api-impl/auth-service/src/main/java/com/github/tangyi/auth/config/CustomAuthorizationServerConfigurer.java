package com.github.tangyi.auth.config;

import com.github.tangyi.common.security.core.ClientDetailsServiceImpl;
import com.github.tangyi.common.security.exceptions.CustomOauthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * 授权服务器配置
 *
 * @author tangyi
 * @date 2019-03-14 11:40
 */
@Configuration
public class CustomAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore() {
        // 将token存储到redis
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "mySecretKey".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
        return converter;
    }

    @Bean
    public ClientDetailsService clientDetails() {
        return new ClientDetailsServiceImpl(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 从数据库加载客户端信息
        clients.withClientDetails(clientDetails());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 将token存储到redis
                .tokenStore(tokenStore())
                // token增强
                .tokenEnhancer(jwtTokenEnhancer())
                // 认证管理器
                .authenticationManager(authenticationManager)
                // 定制异常处理
                .exceptionTranslator(e -> {
                    if (e instanceof OAuth2Exception) {
                        OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
                        return ResponseEntity
                                .status(oAuth2Exception.getHttpErrorCode())
                                .body(new CustomOauthException(oAuth2Exception.getMessage(), oAuth2Exception.getHttpErrorCode()));
                    } else {
                        throw e;
                    }
                });
    }
}

