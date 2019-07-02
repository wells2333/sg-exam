package com.github.tangyi.auth.security;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.enums.LoginType;
import com.github.tangyi.common.security.tenant.TenantContextHolder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * 扩展JwtAccessTokenConverter，增加租户code
 *
 * @author tangyi
 * @date 2019/5/28 22:53
 */
public class CustomTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        String grantType = authentication.getOAuth2Request().getGrantType();
        // 加入tenantCode
        additionalInfo.put("tenantCode", TenantContextHolder.getTenantCode());
        // 加入登录类型，用户名/手机号
        if (grantType.equalsIgnoreCase(CommonConstant.GRANT_TYPE_PASSWORD)) {
            additionalInfo.put("loginType", LoginType.PWD.getType());
        } else if (grantType.equalsIgnoreCase(CommonConstant.GRANT_TYPE_MOBILE)) {
            additionalInfo.put("loginType", LoginType.SMS.getType());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return super.enhance(accessToken, authentication);
    }
}
