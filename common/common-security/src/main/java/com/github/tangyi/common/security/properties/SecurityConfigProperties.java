package com.github.tangyi.common.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * security oauth2相关配置
 *
 * @author tangyi
 * @date 2019-03-14 16:48
 */
@Configuration
@ConfigurationProperties(prefix = "security.properties")
public class SecurityConfigProperties {

    private String clientId;

    private String clientSecret;

    private String grantTypePassword;

    private String authorizationCode;

    private String refreshToken;

    private String implicit;

    private String scopeRead;

    private String scopeWrite;

    private String trust;

    private int accessTokenValiditySeconds;

    private int refreshTokenValiditySeconds;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantTypePassword() {
        return grantTypePassword;
    }

    public void setGrantTypePassword(String grantTypePassword) {
        this.grantTypePassword = grantTypePassword;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getImplicit() {
        return implicit;
    }

    public void setImplicit(String implicit) {
        this.implicit = implicit;
    }

    public String getScopeRead() {
        return scopeRead;
    }

    public void setScopeRead(String scopeRead) {
        this.scopeRead = scopeRead;
    }

    public String getScopeWrite() {
        return scopeWrite;
    }

    public void setScopeWrite(String scopeWrite) {
        this.scopeWrite = scopeWrite;
    }

    public String getTrust() {
        return trust;
    }

    public void setTrust(String trust) {
        this.trust = trust;
    }

    public int getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public int getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }
}

