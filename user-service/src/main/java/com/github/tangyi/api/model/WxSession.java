package com.github.tangyi.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WxSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private String openId;

    private String sessionKey;
}
