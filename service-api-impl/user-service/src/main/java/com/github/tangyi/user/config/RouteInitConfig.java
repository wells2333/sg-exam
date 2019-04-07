package com.github.tangyi.user.config;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.constant.MqConstant;
import com.github.tangyi.common.core.model.Route;
import com.github.tangyi.common.core.utils.JsonMapper;
import com.github.tangyi.user.service.RouteService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 初始化路由信息
 *
 * @author tangyi
 * @date 2019/4/2 18:42
 */
@Configuration
public class RouteInitConfig {

    private static final Logger logger = LoggerFactory.getLogger(RouteInitConfig.class);

    @Autowired
    private RouteService routeService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void initRoute() {
        init();
    }

    /**
     * 加载所有路由，发送到mq，存放到redis
     *
     * @author tangyi
     * @date 2019/04/07 12:05
     */
    public void init() {
        try {
            Route init = new Route();
            init.setStatus(CommonConstant.DEL_FLAG_NORMAL.toString());
            List<Route> routes = routeService.findList(init);
            if (CollectionUtils.isNotEmpty(routes)) {
                logger.info("加载{}条路由记录", routes.size());
                for (Route route : routes) {
                    // 发送消息
                    amqpTemplate.convertAndSend(MqConstant.EDIT_GATEWAY_ROUTE_QUEUE, route);
                }
                // 存入Redis
                redisTemplate.opsForValue().set(CommonConstant.ROUTE_KEY, JsonMapper.getInstance().toJson(routes));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
