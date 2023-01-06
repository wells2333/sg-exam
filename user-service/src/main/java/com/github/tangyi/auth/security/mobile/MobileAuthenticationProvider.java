package com.github.tangyi.auth.security.mobile;

import com.github.tangyi.auth.security.core.event.CustomAuthenticationFailureEvent;
import com.github.tangyi.auth.security.core.event.CustomAuthenticationSuccessEvent;
import com.github.tangyi.auth.security.core.user.CustomUserDetailsService;
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

@Slf4j
@Data
public class MobileAuthenticationProvider implements AuthenticationProvider {

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private CustomUserDetailsService customUserDetailsService;

	@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;
        String principal = mobileAuthenticationToken.getPrincipal().toString();
        UserDetails userDetails = customUserDetailsService.loadUserBySocialAndTenantCode(TenantContextHolder.getTenantCode(), principal, mobileAuthenticationToken.getMobileUser());
        if (userDetails == null) {
            log.info("authentication failed: no credentials provided");
			SpringContextHolder.publishEvent(new CustomAuthenticationFailureEvent(authentication, userDetails));
			throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.noopBindAccount", "Noop Bind Account"));
        }
        MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(mobileAuthenticationToken.getDetails());
		SpringContextHolder.publishEvent((new CustomAuthenticationSuccessEvent(authentication, userDetails)));
		return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
