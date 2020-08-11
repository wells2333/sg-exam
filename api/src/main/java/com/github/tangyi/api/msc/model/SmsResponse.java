package com.github.tangyi.api.msc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 封装短信服务返回的结果
 *
 * @author tangyi
 * @date 2019/08/04 13:51
 */
@Data
public class SmsResponse {

    @JsonProperty("Message")
    private String message;

    @JsonProperty("RequestId")
    private String requestId;

    @JsonProperty("Code")
    private String code;
}
