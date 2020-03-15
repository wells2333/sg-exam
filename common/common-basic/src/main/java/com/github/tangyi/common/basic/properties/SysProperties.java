package com.github.tangyi.common.basic.properties;

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
     * 管理员账号
     */
    private String adminUser;

    /**
     * 密码加密解密的key
     */
    private String key;

    /**
     * 缓存超时时间
     */
    private String cacheExpire;

    private String gatewaySecret;

    /**
     * web端配置的静态封面路径
     */
    private String webAvatar;

    /**
     * web端配置的静态封面数量
     */
    private Integer webAvatarCount;

    /**
     * web端配置的静态封面的后缀名
     */
    private String webAvatarSuffix;

    /**
     * 二维码生成链接
     */
    private String qrCodeUrl;
}
