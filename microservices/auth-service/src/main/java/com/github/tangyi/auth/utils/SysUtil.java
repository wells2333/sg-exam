package com.github.tangyi.auth.utils;

import com.github.tangyi.auth.constant.SecurityConstant;
import com.github.tangyi.common.utils.TenantContextHolder;
import com.github.tangyi.common.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

/**
 * 系统工具类
 *
 * @author tangyi
 * @date 2018-09-13 20:50
 */
@Slf4j
public class SysUtil {

    /**
     * 获取当前登录的用户名
     *
     * @return String
     * @author tangyi
     * @date 2019/03/17 11:46
     */
    public static String getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails)
            return ((UserDetails) principal).getUsername();
        if (principal instanceof Principal)
            return ((Principal) principal).getName();
        return String.valueOf(principal);
    }

    /**
     * 获取系统编号
     *
     * @return String
     */
    public static String getSysCode() {
        return SecurityConstant.SYS_CODE;
    }

    /**
     * 获取租户编号
     *
     * @return String
     */
    public static String getTenantCode() {
        String tenantCode = TenantContextHolder.getTenantCode();
        if (StringUtils.isBlank(tenantCode))
            tenantCode = getCurrentUserTenantCode();
        if (StringUtils.isBlank(tenantCode))
            tenantCode = SecurityConstant.DEFAULT_TENANT_CODE;
        return tenantCode;
    }

    /**
     * 获取当前登录的租户code
     *
     * @return String
     */
    private static String getCurrentUserTenantCode() {
        String tenantCode = "";
        try {
            ResourceServerTokenServices resourceServerTokenServices = SpringContextHolder.getApplicationContext().getBean(
					ResourceServerTokenServices.class);
            Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
            if (details instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;
                OAuth2AccessToken oAuth2AccessToken = resourceServerTokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue());
                Object tenantObj = oAuth2AccessToken.getAdditionalInformation().get(SecurityConstant.TENANT_CODE);
                tenantCode = tenantObj == null ? "" : tenantObj.toString();
            } else if (details instanceof WebAuthenticationDetails) {
                // 未认证
                Object requestObj = RequestContextHolder.getRequestAttributes();
                if (requestObj != null) {
                    HttpServletRequest request = ((ServletRequestAttributes) requestObj).getRequest();
                    tenantCode = request.getParameter(SecurityConstant.TENANT_CODE);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return tenantCode;
    }

    /**
     * 获取当前用户的授权信息
     *
     * @return Authentication
     * @author tangyi
     * @date 2019/03/17 19:18
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前登录用户的授权信息
     *
     * @return Object
     * @author tangyi
     * @date 2019/03/17 11:48
     */
    public static Object getCurrentPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 从header 请求中的clientId/clientsecect
     *
     * @param header header中的参数
     * @throws RuntimeException if the Basic header is not present or is not valid
     *                          Base64
     */
    public static String[] extractAndDecodeHeader(String header) throws IOException {
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to decode basic authentication token");
        }
        String token = new String(decoded, StandardCharsets.UTF_8);
        int delim = token.indexOf(":");
        if (delim == -1)
            throw new RuntimeException("Invalid basic authentication token");
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }
}
