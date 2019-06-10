package com.github.tangyi.gateway.filters;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.gateway.config.PreviewConfig;
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
        // 当前请求
        ServerHttpRequest request = exchange.getRequest();
        // enabled为false、GET请求、忽略的URL，直接向下执行
        log.trace("preview.enabled:{}", previewConfig.isEnabled());
        if (!previewConfig.isEnabled() || StrUtil.equalsIgnoreCase(request.getMethodValue(), HttpMethod.GET.name()) || isIgnore(request.getURI().getPath()))
            return chain.filter(exchange);
        log.warn("演示环境不能操作，{},{}", request.getMethodValue(), request.getURI().getPath());
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.LOCKED);
        return response.setComplete();
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
