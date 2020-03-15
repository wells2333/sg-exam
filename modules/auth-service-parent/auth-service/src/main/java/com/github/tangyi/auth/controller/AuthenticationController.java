package com.github.tangyi.auth.controller;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.exceptions.CommonException;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.web.BaseController;
import com.nimbusds.jose.jwk.JWKSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

	@Autowired
	private JWKSet jwkSet;

	/**
     * 用户信息校验
     *
     * @param authentication 信息
     * @return 用户信息
     */
    @RequestMapping("user")
    public Object user(Authentication authentication) {
        return authentication.getPrincipal();
    }

    /**
     * 清除access_token
     *
     * @param request request
     * @return ReturnT
     */
    @PostMapping("removeToken")
    public ResponseBean<Boolean> removeToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (StringUtils.isBlank(accessToken))
            throw new CommonException("accessToken为空.");
        if (accessToken.startsWith(CommonConstant.AUTHORIZATION_BEARER))
            accessToken = accessToken.split(CommonConstant.AUTHORIZATION_BEARER)[1];
        return new ResponseBean<>(consumerTokenServices.revokeToken(accessToken));
    }

    /**
     *
     * @return Map
     * @author tangyi
     * @date 2020/3/7 4:25 下午
     */
	@GetMapping("/jwks.json")
	public Map<String, Object> keys() {
		return this.jwkSet.toJSONObject();
	}
}
