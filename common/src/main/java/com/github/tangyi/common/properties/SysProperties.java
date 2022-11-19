package com.github.tangyi.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author tangyi
 * @date 2019/4/26 11:54
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sys")
public class SysProperties {

    /**
     * 默认头像
     */
    private String defaultAvatar;

    /**
     * 密码加密解密的key
     */
    private String key;

    /**
     * 缓存超时时间
     */
    private String cacheExpire;

    private String gatewaySecret;

    private String defaultImageType;

    private Integer defaultImageCount;

    /**
     * logo的后缀名
     */
    private String logoSuffix;

    /**
     * 二维码生成链接
     */
    private String qrCodeUrl;

    /**
     * 附件上传目录
     */
    private String attachPath;

    /**
     * 支持预览的附件后缀名，多个用逗号隔开，如：png,jpeg
     */
    private String canPreview;

	private String publicBucket;

	private String privateBucket;
}
