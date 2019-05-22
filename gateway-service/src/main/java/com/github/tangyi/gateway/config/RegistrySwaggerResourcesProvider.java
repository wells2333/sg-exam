package com.github.tangyi.gateway.config;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Swagger聚合文档
 * 目前问题：结合动态更新路由之后，GatewayProperties获取不到新的路由列表，导致swagger-ui显示不了
 *
 * @author tangyi
 * @date 2019/3/26 15:39
 */
@Component
@Primary
@AllArgsConstructor
public class RegistrySwaggerResourcesProvider implements SwaggerResourcesProvider {

    private static final String API_URI = "/v2/api-docs";

    private final RouteLocator routeLocator;

    private final GatewayProperties gatewayProperties;

    private final SwaggerProviderConfig swaggerProviderConfig;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        // 取出gateway的route
        routeLocator.getRoutes().subscribe(route -> {
            // 根据文档提供者过滤
            if (swaggerProviderConfig.getProviders().contains(route.getId()))
                routes.add(route.getId());
        });
        // 结合配置的route-路径(Path)，和route过滤，只获取有效的route节点
        List<RouteDefinition> routeDefinitions = gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId())).collect(Collectors.toList());
        routeDefinitions.forEach(routeDefinition -> {
            List<PredicateDefinition> predicates = routeDefinition.getPredicates().stream()
                    .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName())).collect(Collectors.toList());
            predicates.forEach(predicateDefinition -> {
                resources.add(swaggerResource(routeDefinition.getId(),
                        predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                .replace("/**", API_URI)));
            });
        });
        return resources;
    }

    /**
     * 服务信息
     *
     * @param name     name
     * @param location location
     * @return SwaggerResource
     */
    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
