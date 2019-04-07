package com.github.tangyi.gateway.config;

import com.github.tangyi.common.core.constant.MqConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMq配置
 *
 * @author tangyi
 * @date 2019/4/2 17:56
 */
@Configuration
public class RabbitConfig {

    /**
     * 修改路由
     *
     * @return Queue
     */
    @Bean
    public Queue editQueue() {
        return new Queue(MqConstant.EDIT_GATEWAY_ROUTE_QUEUE);
    }

    /**
     * 删除路由
     *
     * @return Queue
     */
    @Bean
    public Queue delQueue() {
        return new Queue(MqConstant.DEL_GATEWAY_ROUTE_QUEUE);
    }

    /**
     * 刷新路由
     *
     * @return Queue
     */
    @Bean
    public Queue refreshQueue() {
        return new Queue(MqConstant.REFRESH_GATEWAY_ROUTE_QUEUE);
    }
}
