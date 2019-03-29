package com.github.tangyi.gateway.filters;

import cn.hutool.core.util.StrUtil;
import com.github.tangyi.common.core.constant.SecurityConstant;
import com.github.tangyi.common.core.exceptions.InvalidValidateCodeException;
import com.github.tangyi.common.core.exceptions.ValidateCodeExpiredException;
import com.github.tangyi.gateway.constants.GatewayConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
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
@Component
public class ValidateCodeFilter implements GlobalFilter, Ordered {

    private static final String EXPIRED_ERROR = "验证码已过期，请重新获取";

    private static final String PASSWORD = "password";

    private static final String GRANT_TYPE = "grant_type";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 当前请求
        ServerHttpRequest request = exchange.getRequest();
        // 请求的URI
        URI uri = request.getURI();
        if ("POST".equals(request.getMethodValue())
                && StrUtil.containsAnyIgnoreCase(uri.getPath(), GatewayConstant.OAUTH_TOKEN_URL, GatewayConstant.MOBILE_TOKEN_URL)) {
            String grantType = request.getQueryParams().getFirst(GRANT_TYPE);
            // 授权类型为密码模式才校验验证码
            if (PASSWORD.equals(grantType)) {
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
        String key = SecurityConstant.DEFAULT_CODE_KEY + randomStr;
        // 验证码过期
        if (!redisTemplate.hasKey(key))
            throw new ValidateCodeExpiredException(EXPIRED_ERROR);
        Object codeObj = redisTemplate.opsForValue().get(key);
        if (codeObj == null)
            throw new ValidateCodeExpiredException(EXPIRED_ERROR);
        String saveCode = codeObj.toString();
        if (StrUtil.isBlank(saveCode)) {
            redisTemplate.delete(key);
            throw new ValidateCodeExpiredException(EXPIRED_ERROR);
        }
        if (!StrUtil.equals(saveCode, code)) {
            redisTemplate.delete(key);
            throw new InvalidValidateCodeException("验证码错误，请重新输入");
        }
        redisTemplate.delete(key);
    }

}
