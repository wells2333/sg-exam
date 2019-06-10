package com.github.tangyi.user.receiver;

import com.github.tangyi.common.core.constant.MqConstant;
import com.github.tangyi.user.config.RouteInitConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author tangyi
 * @date 2019/4/7 12:38
 */
@Slf4j
@AllArgsConstructor
@Service
public class RouteReceiver {

    private final RouteInitConfig routeInitConfig;

    /**
     * 刷新路由
     *
     * @param msg msg
     * @author tangyi
     * @date 2019/04/07 12:39
     */
    @RabbitListener(queues = {MqConstant.REFRESH_GATEWAY_ROUTE_QUEUE})
    public void refreshRoute(String msg) {
        log.info("刷新路由{}", msg);
        routeInitConfig.init();
    }
}
