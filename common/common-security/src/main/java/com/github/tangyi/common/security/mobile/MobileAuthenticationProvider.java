package com.github.tangyi.common.security.mobile;

import com.github.tangyi.common.security.core.CustomUserDetailsService;
import com.github.tangyi.common.security.event.CustomAuthenticationFailureEvent;
import com.github.tangyi.common.security.event.CustomAuthenticationSuccessEvent;
import com.github.tangyi.common.security.tenant.TenantContextHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author tangyi
 * @date 2019/6/22 21:00
 */
@Slf4j
@Data
public class MobileAuthenticationProvider implements AuthenticationProvider {

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private CustomUserDetailsService customUserDetailsService;

	private ApplicationEventPublisher publisher;

	@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;
        String principal = mobileAuthenticationToken.getPrincipal().toString();
        UserDetails userDetails = customUserDetailsService.loadUserBySocialAndTenantCode(TenantContextHolder.getTenantCode(), principal, mobileAuthenticationToken.getMobileUser());
        if (userDetails == null) {
            log.debug("Authentication failed: no credentials provided");
			publisher.publishEvent(new CustomAuthenticationFailureEvent(authentication, userDetails));
			throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.noopBindAccount", "Noop Bind Account"));
        }
        MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(mobileAuthenticationToken.getDetails());
		publisher.publishEvent(new CustomAuthenticationSuccessEvent(authentication, userDetails));
		return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
