package com.github.tangyi.gateway.config;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.Route;
import com.github.tangyi.common.core.utils.JsonMapper;
import com.github.tangyi.gateway.receiver.GatewayRouteReceiver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态路由实现：修改数据库路由配置，用户服务发送路由更新消息，网关消费消息，更新路由配置
 * 依赖Redis，如果Redis被清空，需要手动刷新加载路由列表
 *
 * @author tangyi
 * @date 2019/4/2 14:40
 */
@Slf4j
@AllArgsConstructor
@Configuration
public class RouteInitConfig {

    private final RedisTemplate redisTemplate;

    private final GatewayRouteReceiver gatewayRouteReceiver;

    @PostConstruct
    public void initRoute() {
        // 重Redis加载路由列表
        Object object = redisTemplate.opsForValue().get(CommonConstant.ROUTE_KEY);
        if (object != null) {
            List<Route> routes = JsonMapper.getInstance().fromJson(object.toString(), JsonMapper.getInstance().createCollectionType(ArrayList.class, Route.class));
            if (CollectionUtils.isNotEmpty(routes)) {
                log.info("加载{}条路由记录", routes.size());
                for (Route route : routes)
                    gatewayRouteReceiver.editRoute(route);
            }
        }
    }
}
