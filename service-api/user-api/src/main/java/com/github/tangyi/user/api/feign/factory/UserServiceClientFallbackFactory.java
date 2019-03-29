package com.github.tangyi.user.api.feign.factory;

import com.github.tangyi.user.api.feign.UserServiceClient;
import com.github.tangyi.user.api.feign.fallback.UserServiceClientFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户断路器工厂
 *
 * @author tangyi
 * @date 2019/3/23 23:38
 */
@Component
public class UserServiceClientFallbackFactory implements FallbackFactory<UserServiceClient> {

    @Override
    public UserServiceClient create(Throwable throwable) {
        UserServiceClientFallbackImpl userServiceClientFallback = new UserServiceClientFallbackImpl();
        userServiceClientFallback.setThrowable(throwable);
        return userServiceClientFallback;
    }
}
