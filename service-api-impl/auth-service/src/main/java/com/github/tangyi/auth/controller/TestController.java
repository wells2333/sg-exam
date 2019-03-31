package com.github.tangyi.auth.controller;

import com.github.tangyi.common.core.model.ResponseBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangyi
 * @date 2019/3/30 14:40
 */
@RestController
// 动态刷新，修改配置后post请求/actuator/refresh
@RefreshScope
public class TestController {

    @Value("${mobile.token}")
    private String mobileToken;

    @RequestMapping("/mobile/token")
    public ResponseBean<String> mobileToken() {
        return new ResponseBean<>(mobileToken);
    }
}
