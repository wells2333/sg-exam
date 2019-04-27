package com.github.tangyi.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author tangyi
 * @date 2019/4/26 11:54
 */
@Configuration
@ConfigurationProperties(prefix = "sys")
public class SysProperties {

    /**
     * 管理员账号
     */
    private String adminUser;

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }
}
