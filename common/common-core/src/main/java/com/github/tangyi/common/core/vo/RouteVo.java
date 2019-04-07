package com.github.tangyi.common.core.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由信息
 *
 * @author tangyi
 * @date 2019/3/27 11:06
 */
public class RouteVo {

    /**
     * 路由的Id
     */
    private String routeId;

    /**
     * 路由名称
     */
    private String routeName;

    /**
     * 路由断言集合配置
     */
    private List<RoutePredicateVo> predicates = new ArrayList<>();

    /**
     * 路由过滤器集合配置
     */
    private List<RouteFilterVo> filters = new ArrayList<>();

    /**
     * 路由规则转发的目标uri
     */
    private String uri;

    /**
     * 路由执行的顺序
     */
    private int order = 0;

    /**
     * 排序
     */
    private String sort;

    /**
     * 启用禁用
     */
    private String status;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public List<RoutePredicateVo> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<RoutePredicateVo> predicates) {
        this.predicates = predicates;
    }

    public List<RouteFilterVo> getFilters() {
        return filters;
    }

    public void setFilters(List<RouteFilterVo> filters) {
        this.filters = filters;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
