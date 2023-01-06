package com.github.tangyi.api.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SmsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 接收人
     */
    private String receiver;

    /**
     * 发送内容
     */
    private String content;
}
