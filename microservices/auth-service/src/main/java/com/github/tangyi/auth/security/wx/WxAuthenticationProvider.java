package com.github.tangyi.auth.security.wx;

import com.github.tangyi.auth.security.core.CustomAuthenticationFailureEvent;
import com.github.tangyi.auth.security.core.CustomAuthenticationSuccessEvent;
import com.github.tangyi.auth.security.core.CustomUserDetailsService;
import com.github.tangyi.common.utils.SpringContextHolder;
import com.github.tangyi.common.utils.TenantContextHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author tangyi
 * @date 2019/07/05 19:34
 */
@Slf4j
@Data
public class WxAuthenticationProvider implements AuthenticationProvider {

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WxAuthenticationToken wxAuthenticationToken = (WxAuthenticationToken) authentication;
        // 微信的code
        String principal = wxAuthenticationToken.getPrincipal().toString();
        UserDetails userDetails = customUserDetailsService.loadUserByWxCodeAndTenantCode(principal, TenantContextHolder.getTenantCode(), wxAuthenticationToken.getWxUser());
        if (userDetails == null) {
            log.debug("Authentication failed: no credentials provided");
			SpringContextHolder.publishEvent(new CustomAuthenticationFailureEvent(authentication, userDetails));
			throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.noopBindAccount", "Noop Bind Account"));
        }
        WxAuthenticationToken authenticationToken = new WxAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(wxAuthenticationToken.getDetails());
		SpringContextHolder.publishEvent(new CustomAuthenticationSuccessEvent(authentication, userDetails));
		return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WxAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
