package com.github.tangyi.auth.security.core;

import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.constant.SecurityConstant;
import com.github.tangyi.common.enums.LoginTypeEnum;
import com.github.tangyi.common.utils.TenantContextHolder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 扩展JwtAccessTokenConverter，增加租户code
 *
 * @author tangyi
 * @date 2019/5/28 22:53
 */
public class CustomTokenConverter extends JwtAccessTokenConverter {

	private RsaSigner signer;

	private Map<String, String> customHeaders;

	private JsonParser objectMapper = JsonParserFactory.create();

	public CustomTokenConverter(KeyPair keyPair, Map<String, String> customHeaders) {
		super();
		super.setKeyPair(keyPair);
		this.signer = new RsaSigner((RSAPrivateKey) keyPair.getPrivate());
		this.customHeaders = customHeaders;
	}

	@Override
	protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		String content;
		try {
			content = this.objectMapper
					.formatMap(getAccessTokenConverter().convertAccessToken(accessToken, authentication));
		} catch (Exception ex) {
			throw new IllegalStateException("Cannot convert access token to JSON", ex);
		}
		return JwtHelper.encode(content, this.signer, this.customHeaders).getEncoded();
	}

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		final Map<String, Object> additionalInfo = new HashMap<>();
		String grantType = authentication.getOAuth2Request().getGrantType();
		// 加入tenantCode
		additionalInfo.put(CommonConstant.TENANT_CODE, TenantContextHolder.getTenantCode());
		// 加入登录类型，用户名/手机号
		String loginType = "";
		if (grantType.equalsIgnoreCase(CommonConstant.GRANT_TYPE_PASSWORD)) {
			loginType = LoginTypeEnum.PWD.getType();
		} else if (grantType.equalsIgnoreCase(CommonConstant.GRANT_TYPE_MOBILE)) {
			loginType = LoginTypeEnum.SMS.getType();
		} else if (grantType.equalsIgnoreCase(LoginTypeEnum.WECHAT.getType())) {
			loginType = LoginTypeEnum.WECHAT.getType();
		}
		additionalInfo.put(SecurityConstant.LOGIN_TYPE, loginType);
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		return super.enhance(accessToken, authentication);
	}
}
