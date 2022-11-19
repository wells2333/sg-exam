package com.github.tangyi.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信网页（h5）配置参数
 * @author tangyi
 * @date 2022/10/29 10:41 上午
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wxh5")
public class WxH5Properties {

	private String appId;

	private String appSecret;

	/**
	 * 回调地址
	 */
	private String serverUrl;

	private String token;
}
