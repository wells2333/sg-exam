package com.github.tangyi.auth.api.module;

import com.github.tangyi.common.core.persistence.BaseEntity;

/**
 * OAuth2客户端信息
 *
 * @author tangyi
 * @date 2019/3/30 16:35
 */
public class OauthClientDetails extends BaseEntity<OauthClientDetails> {

    /**
     * clientId
     */
    private String clientId;

    /**
     * resource_ids
     */
    private String resourceIds;

    /**
     * 密钥明文
     */
    private String clientSecretPlainText;

    /**
     * 密钥密文
     */
    private String clientSecret;

    /**
     * 授权范围
     */
    private String scope;

    /**
     * 授权类型
     */
    private String authorizedGrantTypes;

    /**
     * web_server_redirect_uri
     */
    private String webServerRedirectUri;

    /**
     * authorities
     */
    private String authorities;

    /**
     * access_token有效时间
     */
    private String accessTokenValidity;

    /**
     * refresh_token有效时间
     */
    private String refreshTokenValidity;

    /**
     * additional_information
     */
    private String additionalInformation;

    /**
     * autoapprove
     */
    private String autoapprove;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public String getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(String accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public String getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(String refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getAutoapprove() {
        return autoapprove;
    }

    public void setAutoapprove(String autoapprove) {
        this.autoapprove = autoapprove;
    }

    public String getClientSecretPlainText() {
        return clientSecretPlainText;
    }

    public void setClientSecretPlainText(String clientSecretPlainText) {
        this.clientSecretPlainText = clientSecretPlainText;
    }
}
