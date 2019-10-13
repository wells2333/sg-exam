package com.github.tangyi.gateway.filters;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.gateway.config.PreviewConfig;
import com.github.tangyi.gateway.constants.GatewayConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 演示环境过滤器
 * 如果配置了preview.enabled为true则过滤器生效
 *
 * @author tangyi
 * @date 2019/4/23 10:54
 */
@Slf4j
@AllArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "preview", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PreviewFilter implements GlobalFilter, Ordered {

    private final PreviewConfig previewConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (shouldFilter(request)) {
            log.warn("演示环境不能操作，{},{}", request.getMethodValue(), request.getURI().getPath());
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.LOCKED);
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    /**
     * 是否拦截
     *
     * @param request request
     * @return boolean
     * @author tangyi
     * @date 2019/06/19 20:06
     */
    private boolean shouldFilter(ServerHttpRequest request) {
        // enabled为false
        if (!previewConfig.isEnabled())
            return false;
        // 演示环境下，只拦截对默认租户的修改操作
        if (GatewayConstant.DEFAULT_TENANT_CODE.equals(request.getHeaders().getFirst(GatewayConstant.TENANT_CODE_HEADER))) {
            String method = request.getMethodValue(), uri = request.getURI().getPath();
            // GET请求、POST请求
            if (StrUtil.equalsIgnoreCase(method, HttpMethod.GET.name()))
                return false;
            if (StrUtil.equalsIgnoreCase(method, HttpMethod.POST.name())
                    && !StrUtil.containsIgnoreCase(uri, "delete")
                    && !StrUtil.containsIgnoreCase(uri, "menu"))
                return false;
            // 拦截DELETE请求
            if (StrUtil.equalsIgnoreCase(method, HttpMethod.DELETE.name()) && !StrUtil.containsIgnoreCase(uri, "attachment"))
                return true;
            // URL白名单
            return !isIgnore(uri);
        }
        return false;
    }

    /**
     * 是否忽略URI
     *
     * @param uri uri
     * @return boolean
     * @author tangyi
     * @date 2019/04/23 13:44
     */
    private boolean isIgnore(String uri) {
        List<String> ignoreUrls = previewConfig.getIgnores();
        if (ignoreUrls != null && !ignoreUrls.isEmpty()) {
            for (String ignoreUrl : ignoreUrls) {
                if (StrUtil.containsIgnoreCase(uri, ignoreUrl))
                    return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
