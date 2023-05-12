package com.github.tangyi.user.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnExpression("!'${sms}'.isEmpty()")
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    private String appKey;

    private String appSecret;

    private String regionId;

    private String domain;

    private String signName;

    private String templateCode;

    private String version;

    private String action;
}
