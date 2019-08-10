package com.github.tangyi.gateway.annotation;

import com.github.tangyi.gateway.config.GatewayTokenTransferConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 因为token里带有权限信息，避免直接返回给前端，所以在网关做一层Token转换，返回给前端的只是token ID
 *
 * @author tangyi
 * @date 2019/6/19 01:35
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GatewayTokenTransferConfig.class)
public @interface EnableGatewayTokenTransfer {
}
