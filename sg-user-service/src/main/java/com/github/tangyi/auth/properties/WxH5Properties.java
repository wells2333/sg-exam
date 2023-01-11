package com.github.tangyi.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
