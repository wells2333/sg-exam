package com.github.tangyi.gateway.filters;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.github.tangyi.gateway.constants.GatewayConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;

/**
 * 解密过滤器
 *
 * @author tangyi
 * @date 2019/3/18 11:30
 */
@Configuration
@ConditionalOnProperty(value = "security.encode.key")
public class DecodePasswordFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(DecodePasswordFilter.class);

    private static final String PASSWORD = "password";

    private static final String GRANT_TYPE = "grant_type";

    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/NOPadding";

    /**
     * 约定的key
     */
    @Value("${security.encode.key}")
    private String key;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 当前请求
        ServerHttpRequest request = exchange.getRequest();
        // 请求的URI
        URI uri = request.getURI();
        // 获取token的请求
        if ("POST".equals(request.getMethodValue()) && StrUtil.containsAnyIgnoreCase(uri.getPath(), GatewayConstant.OAUTH_TOKEN_URL, GatewayConstant.MOBILE_TOKEN_URL)) {
            String grantType = request.getQueryParams().getFirst(GRANT_TYPE);
            // 授权类型为密码模式则解密
            if (PASSWORD.equals(grantType)) {
                String password = request.getQueryParams().getFirst(PASSWORD);
                if (password == null || password.isEmpty()) {
                    logger.info("password is empty...");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
                try {
                    // 开始解密
                    password = decryptAES(password, key);
                    password = password.trim();
                    logger.debug("password decrypt success:{}", password);
                } catch (Exception e) {
                    logger.error("password decrypt fail:{}", password);
                }
                URI newUri = UriComponentsBuilder.fromUri(uri).replaceQueryParam(PASSWORD, password).build(true).toUri();
                request = request.mutate().uri(newUri).build();
                return chain.filter(exchange.mutate().request(request).build());
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }

    /**
     * des解密
     *
     * @param data data
     * @param pass pass
     * @return String
     * @author tangyi
     * @date 2019/03/18 11:39
     */
    private static String decryptAES(String data, String pass) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM), new IvParameterSpec(pass.getBytes()));
        byte[] result = cipher.doFinal(Base64.decode(data.getBytes(CharsetUtil.UTF_8)));
        return new String(result, CharsetUtil.UTF_8);
    }
}
