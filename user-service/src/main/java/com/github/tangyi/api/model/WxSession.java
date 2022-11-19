package com.github.tangyi.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 封装微信登录状态
 *
 * @author tangyi
 * @date 2019/07/05 20:35
 */
@Data
@AllArgsConstructor
public class WxSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private String openId;

    private String sessionKey;
}
