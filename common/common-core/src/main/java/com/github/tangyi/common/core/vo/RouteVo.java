package com.github.tangyi.common.core.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由信息
 *
 * @author tangyi
 * @date 2019/3/27 11:06
 */
@Data
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

}
