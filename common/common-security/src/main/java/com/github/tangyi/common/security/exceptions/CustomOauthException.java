package com.github.tangyi.common.security.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.tangyi.common.security.serializer.CustomOauthExceptionSerializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 定制OAuth2Exception
 *
 * @author tangyi
 * @date 2019/3/18 22:36
 */
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {

    /**
     * 错误码
     */
    private int code;

    public CustomOauthException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
