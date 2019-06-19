package com.github.tangyi.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tangyi
 * @date 2019/6/19 01:55
 */
@ConfigurationProperties("gateway.token-transfer")
public class GatewayTokenTransferProperties {

    /**
     * 默认开启
     */
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
