package com.github.tangyi.gateway.handler;

import com.github.tangyi.common.core.constant.ApiMsg;
import com.github.tangyi.common.core.exceptions.InvalidAccessTokenException;
import com.github.tangyi.common.core.exceptions.InvalidValidateCodeException;
import com.github.tangyi.common.core.exceptions.ValidateCodeExpiredException;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * 网关统一异常处理
 *
 * @author tangyi
 * @date 2019/3/18 20:52
 */
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    /**
     * MessageReader
     */
    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

    /**
     * MessageWriter
     */
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

    /**
     * ViewResolvers
     */
    private List<ViewResolver> viewResolvers = Collections.emptyList();

    /**
     * 存储处理异常后的信息
     */
    private ThreadLocal<ResponseBean<?>> exceptionHandlerResult = new ThreadLocal<>();

    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }

    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

    /**
     * 处理逻辑
     *
     * @param exchange exchange
     * @param ex       ex
     * @return Mono
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        String msg = "Internal Server Error";
        // 返回给前端用的状态码
        int keyCode = ApiMsg.KEY_UNKNOWN;
        int msgCode = ApiMsg.ERROR;
        if (ex instanceof NotFoundException) {
        	// 服务不可用
			keyCode = ApiMsg.KEY_SERVICE;
			msgCode = ApiMsg.UNAVAILABLE;
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            msg = responseStatusException.getMessage();
			// 服务响应错误
			keyCode = ApiMsg.KEY_SERVICE;
			msgCode = ApiMsg.ERROR;
        } else if (ex instanceof InvalidValidateCodeException) {
            msg = ex.getMessage();
            // 验证码错误
			keyCode = ApiMsg.KEY_VALIDATE_CODE;
		} else if (ex instanceof ValidateCodeExpiredException) {
            msg = ex.getMessage();
            // 验证码过期
			keyCode = ApiMsg.KEY_VALIDATE_CODE;
			msgCode = ApiMsg.EXPIRED;
        } else if (ex instanceof InvalidAccessTokenException) {
            msg = ex.getMessage();
            // token非法
			keyCode = ApiMsg.KEY_TOKEN;
			msgCode = ApiMsg.INVALID;
        }
        // 封装响应体
        ResponseBean<String> responseBean = new ResponseBean<>(msg, keyCode, msgCode);
        // 错误记录
        ServerHttpRequest request = exchange.getRequest();
        log.error("GatewayExceptionHandler: {}, error: {}", request.getPath(), ex.getMessage());
        if (exchange.getResponse().isCommitted())
            return Mono.error(ex);
        exceptionHandlerResult.set(responseBean);
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
                .switchIfEmpty(Mono.error(ex))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> write(exchange, response));
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        ResponseBean<?> responseBean = exceptionHandlerResult.get();
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(responseBean));
    }

    private Mono<? extends Void> write(ServerWebExchange exchange,
                                       ServerResponse response) {
        exchange.getResponse().getHeaders().setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }

    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return GatewayExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return GatewayExceptionHandler.this.viewResolvers;
        }
    }
}
