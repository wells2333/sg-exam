package com.github.tangyi.gateway.filters;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.exceptions.InvalidValidateCodeException;
import com.github.tangyi.common.core.exceptions.ValidateCodeExpiredException;
import com.github.tangyi.gateway.constants.GatewayConstant;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * 验证码过滤器
 *
 * @author tangyi
 * @date 2019/3/18 16:40
 */
@AllArgsConstructor
@Component
public class ValidateCodeFilter implements GlobalFilter, Ordered {

    private final RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 当前请求
        ServerHttpRequest request = exchange.getRequest();
        // 请求的URI
        URI uri = request.getURI();
        if (HttpMethod.POST.matches(request.getMethodValue())
                && StrUtil.containsAnyIgnoreCase(uri.getPath(), GatewayConstant.OAUTH_TOKEN_URL, GatewayConstant.REGISTER, GatewayConstant.MOBILE_TOKEN_URL)) {
            String grantType = request.getQueryParams().getFirst(GatewayConstant.GRANT_TYPE);
            // 授权类型为密码模式、注册才校验验证码
            if (GatewayConstant.GRANT_TYPE_PASSWORD.equals(grantType) || StrUtil.containsAnyIgnoreCase(uri.getPath(), GatewayConstant.REGISTER)) {
                // 校验验证码
                checkCode(request);
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }

    /**
     * 校验验证码
     *
     * @param serverHttpRequest serverHttpRequest
     * @throws InvalidValidateCodeException
     */
    private void checkCode(ServerHttpRequest serverHttpRequest) throws InvalidValidateCodeException {
        MultiValueMap<String, String> params = serverHttpRequest.getQueryParams();
        String code = params.getFirst("code");
        if (StrUtil.isBlank(code))
            throw new InvalidValidateCodeException("请输入验证码");
        // 获取随机码
        String randomStr = params.getFirst("randomStr");
        if (StrUtil.isBlank(randomStr))
            randomStr = params.getFirst("mobile");
        String key = CommonConstant.DEFAULT_CODE_KEY + randomStr;
        // 验证码过期
        if (!redisTemplate.hasKey(key))
            throw new ValidateCodeExpiredException(GatewayConstant.EXPIRED_ERROR);
        Object codeObj = redisTemplate.opsForValue().get(key);
        if (codeObj == null)
            throw new ValidateCodeExpiredException(GatewayConstant.EXPIRED_ERROR);
        String saveCode = codeObj.toString();
        if (StrUtil.isBlank(saveCode)) {
            redisTemplate.delete(key);
            throw new ValidateCodeExpiredException(GatewayConstant.EXPIRED_ERROR);
        }
        if (!StrUtil.equals(saveCode, code)) {
            redisTemplate.delete(key);
            throw new InvalidValidateCodeException("验证码错误，请重新输入");
        }
        redisTemplate.delete(key);
    }

}
