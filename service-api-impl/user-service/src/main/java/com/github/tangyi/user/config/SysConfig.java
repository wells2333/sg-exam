package com.github.tangyi.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 附件配置
 *
 * @author tangyi
 * @date 2019-02-24 20:06
 */
@Data
@Component
@ConfigurationProperties(prefix = "sys")
public class SysConfig {

    /**
     * fastDfs服务器的HTTP地址
     */
    private String fdfsHttpHost;

    /**
     * 上传地址
     */
    private String uploadUrl;

    /**
     * 默认头像
     */
    private String defaultAvatar;
}
