package com.github.tangyi.auth.controller;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication管理
 *
 * @author tangyi
 * @date 2019/3/18 14:13
 */
@RestController
@RequestMapping("/v1/authentication")
public class AuthenticationController extends BaseController {

    @Autowired
    @Qualifier("consumerTokenServices")
    private ConsumerTokenServices consumerTokenServices;

    /**
     * 用户信息校验
     *
     * @param authentication 信息
     * @return 用户信息
     */
    @RequestMapping("/user")
    public Object user(Authentication authentication) {
        return authentication.getPrincipal();
    }

    /**
     * 清除access_token
     *
     * @param accesstoken access_token
     * @return ReturnT
     */
    @PostMapping("/removeToken")
    public ResponseBean<Boolean> removeToken(@RequestHeader("Authorization") String accesstoken) {
        if (accesstoken.startsWith(CommonConstant.AUTHORIZATION_BEARER))
            accesstoken = accesstoken.split(CommonConstant.AUTHORIZATION_BEARER)[1];
        return new ResponseBean<>(consumerTokenServices.revokeToken(accesstoken));
    }
}
