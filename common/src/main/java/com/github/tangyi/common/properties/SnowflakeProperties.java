package com.github.tangyi.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ID生成配置
 *
 * @author tangyi
 * @date 2019/4/26 10:47
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cluster")
public class SnowflakeProperties {

    /**
     * 工作节点ID
     */
    private String workId;

    /**
     * 数据中心ID
     */
    private String dataCenterId;
}
