package com.github.tangyi.common.core.model;

import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 路由信息
 *
 * @author tangyi
 * @date 2019/4/2 14:52
 */
@Data
public class Route extends BaseEntity<Route> {

    /**
     * 路由ID
     */
    private String routeId;

    /**
     * 路由名称
     */
    private String routeName;

    /**
     * 断言
     */
    private String predicates;

    /**
     * 过滤器
     */
    private String filters;

    /**
     * URI
     */
    private String uri;

    /**
     * 排序
     */
    private String sort;

    /**
     * 启用禁用
     */
    private String status;
}
