package com.github.tangyi.common.security.utils;

import com.github.tangyi.common.security.core.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
}
