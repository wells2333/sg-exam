package com.github.tangyi.gateway.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示环境配置
 *
 * @author tangyi
 * @date 2019/4/23 13:38
 */
@Configuration
@RefreshScope
@ConditionalOnExpression("!'${preview}'.isEmpty()")
@ConfigurationProperties(prefix = "preview")
public class PreviewConfig {

    private boolean enabled;

    private List<String> ignores = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getIgnores() {
        return ignores;
    }

    public void setIgnores(List<String> ignores) {
        this.ignores = ignores;
    }
}
