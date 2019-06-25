package com.github.tangyi.common.security.mobile;

import com.github.tangyi.common.security.constant.SecurityConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 手机登录filter
 *
 * @author tangyi
 * @date 2019/6/22 21:15
 */
public class MobileAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";

    @Getter
    @Setter
    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;

    @Getter
    @Setter
    private boolean postOnly = true;

    @Getter
    @Setter
    private AuthenticationEventPublisher eventPublisher;

    @Getter
    @Setter
    private AuthenticationEntryPoint authenticationEntryPoint;

    public MobileAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstant.MOBILE_TOKEN_URL, HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals(HttpMethod.POST.name()))
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        // 获取手机登录的参数
        String mobile = obtainMobile(request);
        if (mobile == null) {
            mobile = "";
        }
        mobile = mobile.trim();
        // 封装成token
        MobileAuthenticationToken mobileAuthenticationToken = new MobileAuthenticationToken(mobile);
        setDetails(request, mobileAuthenticationToken);
        Authentication authResult = null;
        try {
            // 认证
            authResult = this.getAuthenticationManager().authenticate(mobileAuthenticationToken);
            logger.debug("Authentication success: " + authResult);
            // 认证成功
            eventPublisher.publishAuthenticationSuccess(authResult);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        } catch (Exception failed) {
            SecurityContextHolder.clearContext();
            logger.debug("Authentication request failed: " + failed);
            eventPublisher.publishAuthenticationFailure(new BadCredentialsException(failed.getMessage(), failed), new PreAuthenticatedAuthenticationToken("access-token", "N/A"));
            try {
                authenticationEntryPoint.commence(request, response, new UsernameNotFoundException(failed.getMessage(), failed));
            } catch (Exception e) {
                logger.error("authenticationEntryPoint handle error:{}", failed);
            }
        }
        return authResult;
    }

    private String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    private void setDetails(HttpServletRequest request, MobileAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
