package com.github.tangyi.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.tangyi.gateway.serializer.AccessTokenJacksonSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 封装access_token
 *
 * @author tangyi
 * @date 2019/5/19 15:43
 */
@Data
@JsonSerialize(using = AccessTokenJacksonSerializer.class)
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * access_token
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 创建时间
     */
    private Integer createTime;

    /**
     * 超时时间
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;

    /**
     * jti
     */
    private String jti;

    /**
     * refresh_token
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * scope
     */
    private String scope;

    /**
     * token_type
     */
    @JsonProperty("token_type")
    private String tokenType;
}
