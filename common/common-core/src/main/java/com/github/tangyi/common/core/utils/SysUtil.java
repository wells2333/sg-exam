package com.github.tangyi.common.core.utils;

import com.github.tangyi.common.security.constant.SecurityConstant;
import com.github.tangyi.common.security.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
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

    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/NOPadding";

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
            ResourceServerTokenServices resourceServerTokenServices = SpringContextHolder.getApplicationContext().getBean(ResourceServerTokenServices.class);
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
     * des解密
     *
     * @param data data
     * @param pass pass
     * @return String
     * @author tangyi
     * @date 2019/03/18 11:39
     */
    public static String decryptAES(String data, String pass) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM), new IvParameterSpec(pass.getBytes()));
        byte[] result = cipher.doFinal(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
        return new String(result, StandardCharsets.UTF_8);
    }
}
