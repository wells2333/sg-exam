package com.github.tangyi.gateway.receiver;

import com.fasterxml.jackson.databind.JavaType;
import com.github.tangyi.common.core.constant.MqConstant;
import com.github.tangyi.common.core.model.Route;
import com.github.tangyi.common.core.utils.JsonMapper;
import com.github.tangyi.common.core.vo.RouteFilterVo;
import com.github.tangyi.common.core.vo.RoutePredicateVo;
import com.github.tangyi.gateway.service.DynamicRouteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态路由
 *
 * @author tangyi
 * @date 2019/4/2 18:07
 */
@Slf4j
@AllArgsConstructor
@Service
public class GatewayRouteReceiver {

    private final DynamicRouteService dynamicRouteService;

    /**
     * 修改路由
     *
     * @param route route
     * @author tangyi
     * @date 2019/04/02 20:51
     */
    @RabbitListener(queues = {MqConstant.EDIT_GATEWAY_ROUTE_QUEUE})
    public void editRoute(Route route) {
        if (route.getRouteId() == null)
            throw new IllegalArgumentException("routeId不能为空！");
        log.info("更新{}路由", route.getRouteId());
        dynamicRouteService.update(routeDefinition(route));
    }

    /**
     * 删除路由
     *
     * @param routes routes
     * @author tangyi
     * @date 2019/04/02 20:51
     */
    @RabbitListener(queues = {MqConstant.DEL_GATEWAY_ROUTE_QUEUE})
    public void delRoute(List<Route> routes) {
        if (routes == null || routes.isEmpty())
            return;
        for (Route route : routes) {
            if (route.getRouteId() == null)
                throw new IllegalArgumentException("routeId不能为空！");
            log.info("删除{}路由", route.getRouteId());
            dynamicRouteService.delete(route.getRouteId());
        }
    }

    /**
     * 初始化RouteDefinition
     *
     * @param route route
     * @return RouteDefinition
     * @author tangyi
     * @date 2019/04/02 18:50
     */
    private RouteDefinition routeDefinition(Route route) {
        RouteDefinition routeDefinition = new RouteDefinition();
        // id
        routeDefinition.setId(route.getRouteId());

        // predicates
        if (StringUtils.isNotBlank(route.getPredicates()))
            routeDefinition.setPredicates(predicateDefinitions(route));

        // filters
        if (StringUtils.isNotBlank(route.getFilters())) {
            routeDefinition.setFilters(filterDefinitions(route));
        }
        // uri
        routeDefinition.setUri(URI.create(route.getUri()));
        return routeDefinition;
    }

    /**
     * @param route route
     * @return List
     * @author tangyi
     * @date 2019/04/02 21:28
     */
    private List<PredicateDefinition> predicateDefinitions(Route route) {
        List<PredicateDefinition> predicateDefinitions = new ArrayList<>();
        try {
            List<RoutePredicateVo> routePredicateVoList = JsonMapper.getInstance().fromJson(route.getPredicates(),
                    JsonMapper.getInstance().createCollectionType(ArrayList.class, RoutePredicateVo.class));
            if (CollectionUtils.isNotEmpty(routePredicateVoList)) {
                for (RoutePredicateVo routePredicateVo : routePredicateVoList) {
                    PredicateDefinition predicate = new PredicateDefinition();
                    predicate.setArgs(routePredicateVo.getArgs());
                    predicate.setName(routePredicateVo.getName());
                    predicateDefinitions.add(predicate);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return predicateDefinitions;
    }

    /**
     * @param route route
     * @return List
     * @author tangyi
     * @date 2019/04/02 21:29
     */
    private List<FilterDefinition> filterDefinitions(Route route) {
        List<FilterDefinition> filterDefinitions = new ArrayList<>();
        try {
            JavaType javaType = JsonMapper.getInstance().createCollectionType(ArrayList.class, RouteFilterVo.class);
            List<RouteFilterVo> gatewayFilterDefinitions = JsonMapper.getInstance().fromJson(route.getFilters(), javaType);
            if (CollectionUtils.isNotEmpty(gatewayFilterDefinitions)) {
                for (RouteFilterVo gatewayFilterDefinition : gatewayFilterDefinitions) {
                    FilterDefinition filterDefinition = new FilterDefinition();
                    filterDefinition.setName(gatewayFilterDefinition.getName());
                    filterDefinition.setArgs(gatewayFilterDefinition.getArgs());
                    filterDefinitions.add(filterDefinition);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return filterDefinitions;
    }
}
