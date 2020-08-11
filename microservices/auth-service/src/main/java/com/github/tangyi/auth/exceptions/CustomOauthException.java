package com.github.tangyi.auth.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.tangyi.auth.serializer.CustomOauthExceptionSerializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义OauthException
 *
 * @author tangyi
 * @date 2020/2/29 14:12
 */
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {
    public CustomOauthException(String msg) {
        super(msg);
    }
}
