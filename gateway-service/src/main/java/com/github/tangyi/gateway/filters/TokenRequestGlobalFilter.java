package com.github.tangyi.gateway.filters;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.exceptions.InvalidAccessTokenException;
import com.github.tangyi.common.core.utils.JsonMapper;
import com.github.tangyi.gateway.constants.GatewayConstant;
import com.github.tangyi.gateway.model.AccessToken;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 拦截登录/oauth/token请求，封装授权服务返回的access_token为AccessToken，建立jti-access_token的关联关系，返回token里的jti
 *
 * @author tangyi
 * @date 2019/5/19 15:03
 */
@Slf4j
public class TokenRequestGlobalFilter implements GlobalFilter, Ordered {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 连接器
     */
    private static Joiner joiner = Joiner.on("");

    /**
     * refresh_token超时时间，默认30天
     */
    private static long REFRESH_TOKEN_EXPIRE = 60 * 60 * 24 * 30;

    public TokenRequestGlobalFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public int getOrder() {
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 当前请求
        ServerHttpRequest request = exchange.getRequest();
        if (match(request)) {
            String grantType = request.getQueryParams().getFirst(GatewayConstant.GRANT_TYPE);
            // 授权类型为密码、手机号、微信openId
            if (matchGrantType(grantType)) {
                return chain.filter(exchange.mutate().response(responseDecorator(exchange)).build());
            }
        }
        return chain.filter(exchange);
    }

    /**
     * 修改响应体
     *
     * @param exchange exchange
     * @return ServerHttpResponseDecorator
     */
    private ServerHttpResponseDecorator responseDecorator(ServerWebExchange exchange) {
        // 装饰器
        return new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (getStatusCode() != null && getStatusCode().equals(HttpStatus.OK) && body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        long start = System.currentTimeMillis();
                        List<String> contentList = Lists.newArrayList();
                        dataBuffers.forEach(dataBuffer -> {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            // 释放缓存
                            DataBufferUtils.release(dataBuffer);
                            contentList.add(new String(content, StandardCharsets.UTF_8));
                        });
                        // 因为返回的数据量大时，获取到的buffer是不完整的，所以需要连接多个buffer
                        String accessTokenContent = joiner.join(contentList);
                        AccessToken accessToken = JsonMapper.getInstance().fromJson(accessTokenContent, AccessToken.class);
                        if (accessToken == null || StringUtils.isBlank(accessToken.getJti()))
                            throw new InvalidAccessTokenException("token转换失败！");
                        // 真正的access_token和refresh_token
                        String realAccessToken = accessToken.getAccessToken(), realRefreshToken = accessToken.getRefreshToken();
                        // 转换token
                        String converted = JsonMapper.getInstance().toJson(accessToken);
                        // 将真正的access_token，refresh_token存入Redis，建立jti->access_token的映射关系，失效时间为token的失效时间
                        // 暂时用Redis
                        redisTemplate.opsForValue().set(GatewayConstant.GATEWAY_ACCESS_TOKENS + accessToken.getJti(), realAccessToken, accessToken.getExpiresIn(), TimeUnit.SECONDS);
                        redisTemplate.opsForValue().set(GatewayConstant.GATEWAY_REFRESH_TOKENS + accessToken.getJti(), realRefreshToken, REFRESH_TOKEN_EXPIRE, TimeUnit.SECONDS);
                        log.debug("转换token和建立映射关系成功，耗时：{}ms", System.currentTimeMillis() - start);
                        return bufferFactory().wrap(converted.getBytes());
                    }));
                }
                return super.writeWith(body);
            }
        };
    }

    /**
     * 是否过滤
     *
     * @param request request
     * @return boolean
     */
    private boolean match(ServerHttpRequest request) {
        return HttpMethod.POST.matches(request.getMethodValue())
                && StrUtil.containsAnyIgnoreCase(request.getURI().getPath(), GatewayConstant.OAUTH_TOKEN_URL, GatewayConstant.MOBILE_TOKEN_URL, GatewayConstant.WX_TOKEN_URL);
    }

    /**
     * 需要过滤的授权类型
     *
     * @param grantType grantType
     * @return boolean
     */
    private boolean matchGrantType(String grantType) {
        return CommonConstant.GRANT_TYPE_PASSWORD.equals(grantType) || CommonConstant.GRANT_TYPE_MOBILE.equals(grantType) || CommonConstant.GRANT_TYPE_WX.equals(grantType) || GatewayConstant.GRANT_TYPE_REFRESH_TOKEN.equals(grantType);
    }
}
