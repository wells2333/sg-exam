package com.github.tangyi.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 附件配置
 *
 * @author tangyi
 * @date 2019-02-24 20:06
 */
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

    public String getDefaultAvatar() {
        return defaultAvatar;
    }

    public void setDefaultAvatar(String defaultAvatar) {
        this.defaultAvatar = defaultAvatar;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getFdfsHttpHost() {
        return fdfsHttpHost;
    }

    public void setFdfsHttpHost(String fdfsHttpHost) {
        this.fdfsHttpHost = fdfsHttpHost;
    }
}
