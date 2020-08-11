package com.github.tangyi.auth.security.core;

import com.github.tangyi.common.utils.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author tangyi
 * @date 2020-04-19 11:32
 */
public class CustomUserDetailsByNameServiceWrapper<T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {
    private UserDetailsService userDetailsService = null;

    public CustomUserDetailsByNameServiceWrapper() {
    }

    public CustomUserDetailsByNameServiceWrapper(UserDetailsService userDetailsService) {
        Assert.notNull(userDetailsService, "userDetailsService cannot be null.");
        this.userDetailsService = userDetailsService;
    }

    public void afterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "UserDetailsService must be set");
    }

    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
        return this.userDetailsService.loadUserByUsername(authentication.getName());
    }

    public void setUserDetailsService(UserDetailsService aUserDetailsService) {
        this.userDetailsService = aUserDetailsService;
    }
}