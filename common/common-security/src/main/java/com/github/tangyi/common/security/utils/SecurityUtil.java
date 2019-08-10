package com.github.tangyi.common.security.utils;

import com.github.tangyi.common.security.core.UserDetailsImpl;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.security.Principal;

/**
 * 安全工具类
 *
 * @author tangyi
 * @date 2019/3/17 11:44
 */
public class SecurityUtil {

    /**
     * 获取当前用户的租户标识
     *
     * @return String
     * @author tangyi
     * @date 2019/05/25 14:19
     */
    public static String getCurrentUserTenantCode() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal instanceof UserDetails ? ((UserDetailsImpl) principal).getTenantCode() : "";
    }

    /**
     * 获取当前登录的用户名
     *
     * @return String
     * @author tangyi
     * @date 2019/03/17 11:46
     */
    public static String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails)
            return ((UserDetails) principal).getUsername();
        if (principal instanceof Principal)
            return ((Principal) principal).getName();
        return String.valueOf(principal);
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
        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to decode basic authentication token");
        }
        String token = new String(decoded, "UTF8");
        int delim = token.indexOf(":");
        if (delim == -1)
            throw new RuntimeException("Invalid basic authentication token");
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }
}
