package com.github.tangyi.gateway.filters;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.gateway.constants.GatewayConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * 和TokenRequestGlobalFilter配合使用
 * 拦截登录请求，根据jti替换Authorization请求头为真正的access_token
 *
 * @author tangyi
 * @date 2019/5/19 15:03
 */
@Slf4j
@Component
public class TokenResponseGlobalFilter implements GlobalFilter, Ordered {

    private final RedisTemplate redisTemplate;

    @Autowired
    public TokenResponseGlobalFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public int getOrder() {
        return -3;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 当前请求
        ServerHttpRequest request = exchange.getRequest();
        // 请求的URI
        URI uri = request.getURI();
        // 获取Authorization请求头
        String authorization = request.getHeaders().getFirst(CommonConstant.REQ_HEADER);
        // 先判断是否为刷新token请求
        if (StrUtil.containsAnyIgnoreCase(uri.getPath(), GatewayConstant.OAUTH_TOKEN_URL, GatewayConstant.MOBILE_TOKEN_URL)) {
            String grantType = request.getQueryParams().getFirst(GatewayConstant.GRANT_TYPE);
            // 授权类型为refresh_token
            if (GatewayConstant.GRANT_TYPE_REFRESH_TOKEN.equals(grantType)) {
                // 获取请求里refresh_token
                String refreshToken = request.getQueryParams().getFirst(GatewayConstant.GRANT_TYPE_REFRESH_TOKEN);
                // 根据jti从Redis里获取真正的refresh_token
                Object object = redisTemplate.opsForValue().get(GatewayConstant.GATEWAY_REFRESH_TOKENS + refreshToken);
                refreshToken = object == null ? refreshToken : object.toString();
                log.trace("refreshToken:{}", refreshToken);
                // 替换refresh_token参数
                URI newUri = UriComponentsBuilder.fromUri(uri).replaceQueryParam(GatewayConstant.GRANT_TYPE_REFRESH_TOKEN, refreshToken).build(true).toUri();
                log.trace("newUri: {}", newUri);
                ServerHttpRequest newRequest = exchange.getRequest().mutate().uri(newUri).build();
                return chain.filter(exchange.mutate().request(newRequest).build());
            }
        } else if (StringUtils.isNotBlank(authorization) && authorization.startsWith(CommonConstant.TOKEN_SPLIT)) {
            authorization = authorization.replace(CommonConstant.TOKEN_SPLIT, "");
            log.trace("jti authorization: {}", authorization);
            // 从Redis里获取实际的access_token
            Object object = redisTemplate.opsForValue().get(GatewayConstant.GATEWAY_ACCESS_TOKENS + authorization);
            authorization = object == null ? authorization : CommonConstant.TOKEN_SPLIT + object.toString();
            String realAuthorization = authorization;
            log.trace("jti->token：{}", realAuthorization);
            // 更新请求头，参考源码:SetRequestHeaderGatewayFilterFactory
            ServerHttpRequest newRequest = request.mutate().headers(httpHeaders -> httpHeaders.set(CommonConstant.REQ_HEADER, realAuthorization)).build();
            log.trace("newRequestHeader:{}", newRequest.getHeaders().getFirst(CommonConstant.REQ_HEADER));
            return chain.filter(exchange.mutate().request(newRequest).build());
        }
        return chain.filter(exchange);
    }
}
