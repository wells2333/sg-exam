package com.github.tangyi.user.receiver;

import com.github.tangyi.common.core.constant.MqConstant;
import com.github.tangyi.user.config.RouteInitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangyi
 * @date 2019/4/7 12:38
 */
@Service
public class RouteReceiver {

    private static final Logger logger = LoggerFactory.getLogger(RouteReceiver.class);

    @Autowired
    private RouteInitConfig routeInitConfig;

    /**
     * 刷新路由
     *
     * @param msg msg
     * @author tangyi
     * @date 2019/04/07 12:39
     */
    @RabbitListener(queues = {MqConstant.REFRESH_GATEWAY_ROUTE_QUEUE})
    public void refreshRoute(String msg) {
        logger.info("刷新路由{}", msg);
        routeInitConfig.init();
    }
}
