package com.github.tangyi.gateway.controller;

import com.github.tangyi.common.core.constant.MqConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.vo.RouteFilterVo;
import com.github.tangyi.common.core.vo.RoutePredicateVo;
import com.github.tangyi.common.core.vo.RouteVo;
import com.github.tangyi.gateway.service.DynamicRouteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态路由Controller
 *
 * @author tangyi
 * @date 2019/3/27 10:59
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/route")
public class GatewayRouteController {

    private final RouteDefinitionLocator routeDefinitionLocator;

    private final RouteLocator routeLocator;

    private final DynamicRouteService dynamicRouteService;

    private final AmqpTemplate amqpTemplate;

    /**
     * 获取路由信息列表
     *
     * @return Mono
     */
    @GetMapping("/routeList")
    public Mono<List<Map<String, Object>>> routes() {
        Mono<Map<String, RouteDefinition>> routeDefs = this.routeDefinitionLocator
                .getRouteDefinitions().collectMap(RouteDefinition::getId);
        Mono<List<Route>> routes = this.routeLocator.getRoutes().collectList();
        return Mono.zip(routeDefs, routes).map(tuple -> {
            Map<String, RouteDefinition> defs = tuple.getT1();
            List<Route> routeList = tuple.getT2();
            List<Map<String, Object>> allRoutes = new ArrayList<>();
            routeList.forEach(route -> {
                HashMap<String, Object> r = new HashMap<>();
                r.put("route_id", route.getId());
                r.put("order", route.getOrder());
                if (defs.containsKey(route.getId())) {
                    r.put("route_definition", defs.get(route.getId()));
                } else {
                    HashMap<String, Object> obj = new HashMap<>();
                    obj.put("predicate", route.getPredicate().toString());
                    if (!route.getFilters().isEmpty()) {
                        ArrayList<String> filters = new ArrayList<>();
                        for (GatewayFilter filter : route.getFilters()) {
                            filters.add(filter.toString());
                        }
                        obj.put("filters", filters);
                    }
                    if (!obj.isEmpty()) {
                        r.put("route_object", obj);
                    }
                }
                allRoutes.add(r);
            });
            return allRoutes;
        });
    }

    /**
     * 增加路由
     *
     * @param gatewayRouteDefinition gatewayRouteDefinition
     * @return ResponseBean
     */
    @PostMapping
    public ResponseBean<String> add(@RequestBody RouteVo gatewayRouteDefinition) {
        try {
            RouteDefinition definition = assembleRouteDefinition(gatewayRouteDefinition);
            log.info("新增路由：{}，{}", definition.getId(), definition);
            return new ResponseBean<>(this.dynamicRouteService.add(definition));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseBean<>("success");
    }

    /**
     * 删除路由
     *
     * @param id id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable String id) {
        log.info("删除路由：{}", id);
        return this.dynamicRouteService.delete(id);
    }

    /**
     * 更新路由
     *
     * @param routeVo routeVo
     * @return ResponseBean
     */
    @PutMapping
    public ResponseBean<String> update(@RequestBody RouteVo routeVo) {
        RouteDefinition definition = assembleRouteDefinition(routeVo);
        return new ResponseBean<>(this.dynamicRouteService.update(definition));
    }

    /**
     * 刷新路由
     *
     * @return ResponseBean
     * @author tangyi
     * @date 2019/04/07 12:32
     */
    @GetMapping("/refresh")
    public ResponseBean<Boolean> refresh() {
        amqpTemplate.convertAndSend(MqConstant.REFRESH_GATEWAY_ROUTE_QUEUE, "refresh");
        return new ResponseBean<>(Boolean.TRUE);
    }

    /**
     * @param routeVo routeVo
     * @return RouteDefinition
     */
    private RouteDefinition assembleRouteDefinition(RouteVo routeVo) {
        RouteDefinition definition = new RouteDefinition();
        // id
        definition.setId(routeVo.getRouteId());
        List<PredicateDefinition> predicateDefinitions = new ArrayList<>();
        // predicates
        for (RoutePredicateVo routePredicateVo : routeVo.getPredicates()) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(routePredicateVo.getArgs());
            predicate.setName(routePredicateVo.getName());
            predicateDefinitions.add(predicate);
        }
        definition.setPredicates(predicateDefinitions);
        // filters
        List<FilterDefinition> filterDefinitions = new ArrayList<>();
        for (RouteFilterVo routeFilterVo : routeVo.getFilters()) {
            FilterDefinition filterDefinition = new FilterDefinition();
            filterDefinition.setName(routeFilterVo.getName());
            filterDefinition.setArgs(routeFilterVo.getArgs());
            filterDefinitions.add(filterDefinition);
        }
        definition.setFilters(filterDefinitions);
        // uri
        definition.setUri(URI.create(routeVo.getUri()));
        return definition;
    }
}
